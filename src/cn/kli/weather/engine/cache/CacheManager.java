package cn.kli.weather.engine.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import cn.kli.utils.dao.BaseDatabaseHelper;
import cn.kli.weather.engine.City;
import cn.kli.weather.engine.Weather;

public class CacheManager {
	private BaseDatabaseHelper mDbHelper;
	private Context mContext;
	
	public CacheManager(Context context){
	    mContext = context;
		mDbHelper = new WeatherDbHelper(context);
	}
	
	/**
	 * 获取所有收藏的城市
	 * @Title: getMarkedCities
	 * @return
	 * @return List<City>
	 * @date 2014-3-29 下午4:38:11
	 */
	public List<City> getMarkedCities(){
		return getCityDao().findAll();
	}
    
    /**
     * 根据cityId获取收藏城市
     * @Title: getMarkedCityById
     * @param cityId
     * @return
     * @return City
     * @date 2014-3-29 下午4:50:26
     */
    private City getMarkedByCityIndex(String cityIndex){
        CityDao dao = getCityDao();
        City cityMarked = dao.findBy("city_index", cityIndex);
        return cityMarked;
    }
    
    /**
     * 收藏城市
     * @Title: markCity
     * @param city
     * @param source
     * @return void
     * @date 2014-3-29 下午4:38:41
     */
    public void markCity(City city){
        CityDao dao = getCityDao();
        City cityMarked = getMarkedByCityIndex(city.index);
        if(cityMarked == null){
            dao.insert(city);
        }
    }
    
    public void removeCity(City city){
        CityDao dao = getCityDao();
        dao.deleteBy("city_index", city.index);
    }
    
    /**
     * 保存天气信息
     * @Title: cacheWeather
     * @param city
     * @return void
     * @date 2014-3-29 下午4:40:49
     */
    public void cacheWeather(City city){
        WeatherDao dao = getWeatherDao();
        for(Weather weather : city.weathers){
            dao.delete("city_id=? and calendar=?", new String[]{weather.city_id, weather.calendar.getTimeInMillis()+""});
            dao.insert(weather);
        }
    }
    
	private CityDao getCityDao(){
	    return new CityDao(mContext, mDbHelper);
	}
	
	private WeatherDao getWeatherDao(){
	    return new WeatherDao(mContext, mDbHelper);
	}
	
	
	/**
	 * 获取城市和该城市缓存的天气信息
	 * @Title: getCityWithWeather
	 * @param city
	 * @return
	 * @return City
	 * @date 2014-3-29 下午4:40:19
	 */
	public City getCityWithWeather(City city) {
	    if(city == null){
	        return city;
	    }

		ArrayList<Weather> cachedWeathers = new ArrayList<Weather>();
		@SuppressWarnings("unchecked")
        List<Weather> allWeathers = (List<Weather>) getWeatherDao().findBy("city_index", city.index);
		Calendar calNow = Calendar.getInstance();
		if (allWeathers != null && allWeathers.size() > 0) {
		    for(Weather weather : allWeathers){
		        if(sameDay(weather.calendar, calNow)){
                    cachedWeathers.add(weather);
                    calNow.add(Calendar.DAY_OF_MONTH, 1);
		        }
		    }
		}
		
		if(cachedWeathers.size() > 1){
			city.weathers = cachedWeathers;
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
	
}
