package cn.kli.weather.engine.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cn.kli.utils.CollectionUtils;
import cn.kli.utils.klilog;
import cn.kli.weather.engine.City;
import cn.kli.weather.engine.Weather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheManager {
	private DbHelper mDbHelper;
	
	public CacheManager(Context context){
		mDbHelper = new DbHelper(context);
	}
	
	public List<City> getMarkedCities(){
		List<City> cityList = new ArrayList<City>();
		
		return null;
	}
	
	public void markDefaultCity(City city, String source){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(DbHelper.TABLE_MARK_CITY, DbHelper.CITY_ORDER+"=?", new String[]{"0"});
		
		ContentValues cv = new ContentValues();
		cv.put(DbHelper.CITY_NAME, city.name);
		cv.put(DbHelper.CITY_INDEX, city.index);
		cv.put(DbHelper.CITY_SOURCE, source);
		cv.put(DbHelper.CITY_ORDER, 0);
		db.insert(DbHelper.TABLE_MARK_CITY, null, cv);
	}
	
	public boolean hasDefaultCity(){
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor city_cursor = db.query(DbHelper.TABLE_MARK_CITY, null,
				null, null, null, null,
				DbHelper.CITY_ORDER + " asc");
		return city_cursor.moveToFirst();
	}
	
	public City getDefaultCity() {
		City city = new City();
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor city_cursor = db.query(DbHelper.TABLE_MARK_CITY, null,
				null, null, null, null,
				DbHelper.CITY_ORDER + " asc");
		if (city_cursor.moveToFirst()) {
			city.index = city_cursor.getString(city_cursor
					.getColumnIndex(DbHelper.CITY_INDEX));
			city.name = city_cursor.getString(city_cursor
					.getColumnIndex(DbHelper.CITY_NAME));
		} else {
			return null;
		}

		ArrayList<Weather> cachedWeathers = new ArrayList<Weather>();
		Cursor weather_cursor = db.query(DbHelper.TABLE_WEATHER_CACHE, null,
				DbHelper.WEATHER_INDEX + "=?", new String[] { city.index },
				null, null, DbHelper.WEATHER_DATE + " asc");
		Calendar calNow = Calendar.getInstance();
		if (weather_cursor.moveToFirst()) {
			do{
				long time = weather_cursor.getLong(weather_cursor
						.getColumnIndex(DbHelper.WEATHER_DATE));
				Calendar calweather = new GregorianCalendar();
				calweather.setTimeInMillis(time);

				if (sameDay(calweather, calNow)) {

					Weather weather = new Weather();
					weather.currentTemp = weather_cursor.getString(weather_cursor
							.getColumnIndex(DbHelper.WEATHER_TEMP));
					weather.maxTemp = weather_cursor.getString(weather_cursor
							.getColumnIndex(DbHelper.WEATHER_MAX_TEMP));
					weather.minTemp = weather_cursor.getString(weather_cursor
							.getColumnIndex(DbHelper.WEATHER_MIN_TEMP));
					String code = weather_cursor.getString(weather_cursor
							.getColumnIndex(DbHelper.WEATHER_WEATHER));
					weather.weather = decodeWeather(code);
					weather.wind = weather_cursor.getString(weather_cursor
							.getColumnIndex(DbHelper.WEATHER_WIND));
					weather.calendar = calweather;
					cachedWeathers.add(weather);
					
					calNow.add(Calendar.DAY_OF_MONTH, 1);
				}
			}while(weather_cursor.moveToNext());
		}
		
		if(cachedWeathers.size() > 1){
			city.weather = cachedWeathers;
		}

		return city;
	}
	
	private static boolean sameDay(Calendar a, Calendar b){
		if(a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH)
				&& a.get(Calendar.MONTH) == b.get(Calendar.MONTH)
				&& a.get(Calendar.YEAR) == b.get(Calendar.YEAR)){
			return true;
		}
		return false;
	}
	
	public void cacheWeather(City city){
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		for(Weather weather : city.weather){
			ContentValues cv = new ContentValues();
			cv.put(DbHelper.WEATHER_INDEX, city.index);
			cv.put(DbHelper.WEATHER_TEMP, weather.currentTemp);
			cv.put(DbHelper.WEATHER_MAX_TEMP, weather.maxTemp);
			cv.put(DbHelper.WEATHER_MIN_TEMP, weather.minTemp);
			cv.put(DbHelper.WEATHER_WEATHER, codeWeather(weather.weather));
			cv.put(DbHelper.WEATHER_WIND, weather.wind);
			cv.put(DbHelper.WEATHER_DATE, weather.calendar.getTimeInMillis());
			cv.put(DbHelper.WEATHER_UPDATE_TIME, System.currentTimeMillis());
			db.delete(DbHelper.TABLE_WEATHER_CACHE, DbHelper.WEATHER_INDEX+
					"=? and "+DbHelper.WEATHER_DATE+"=?", 
					new String[]{city.index, weather.calendar.getTimeInMillis()+""});
			db.insert(DbHelper.TABLE_WEATHER_CACHE, null, cv);
		}
		db.close();
	}
	
	private String codeWeather(int[] weather){
		if(weather == null){
			return null;
		}else{
			String res = "";
			for(int i = 0; i < weather.length; i++){
				res += weather[i];
				if(i != weather.length - 1){
					res += ",";
				}
			}
			return res;
		}
	}
	
	private int[] decodeWeather(String code){
		String[] codes = code.split(",");
		return CollectionUtils.stringArray2IntArray(codes);
	}
	
	

	private class DbHelper extends SQLiteOpenHelper {
		private final static String DB_NAME = "weatherdatabase";
		private final static int DB_VERSION = 1;
		
		//mark city
		private final static String TABLE_MARK_CITY = "markcity";
		private final static String CITY_NAME = "name";
		private final static String CITY_INDEX = "_index";
		private final static String CITY_SOURCE = "source";
		private final static String CITY_ORDER = "_order";
		private final static String CITY_UPDATE_TIME = "_update";
		
		//city weather
		private final static String TABLE_WEATHER_CACHE = "weathercache";
		private final static String WEATHER_INDEX = "_index"; //index of city
		private final static String WEATHER_TEMP = "temp";
		private final static String WEATHER_MAX_TEMP = "max_temp";
		private final static String WEATHER_MIN_TEMP = "min_temp";
		private final static String WEATHER_WEATHER = "weather";
		private final static String WEATHER_WIND = "wind";
		private final static String WEATHER_DATE = "date";
		private final static String WEATHER_UPDATE_TIME = "_update";
		
		public DbHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String create_city = "create table " + TABLE_MARK_CITY+"("+
					CITY_NAME + " text,"+
					CITY_INDEX + " text,"+
					CITY_SOURCE + " text,"+
					CITY_ORDER + " integer, "+
					CITY_UPDATE_TIME + " timestamp)";
			klilog.info(create_city);
			db.execSQL(create_city);
			
			String create_weather = "create table "+TABLE_WEATHER_CACHE+"("+
					WEATHER_INDEX + " text, "+
					WEATHER_TEMP + " text, "+
					WEATHER_MAX_TEMP + " text, "+
					WEATHER_MIN_TEMP + " text, "+
					WEATHER_WEATHER + " text, "+
					WEATHER_WIND + " text, "+
					WEATHER_DATE + " timestamp, "+
					WEATHER_UPDATE_TIME + " timestamp)";
			klilog.info(create_weather);
			db.execSQL(create_weather);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

		}
	}
}
