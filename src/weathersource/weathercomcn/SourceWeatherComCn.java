package weathersource.weathercomcn;

import java.util.ArrayList;
import java.util.List;

import weathersource.InternetAccess;

import android.content.Context;
import android.text.TextUtils;
import cn.kli.utils.klilog;
import cn.kli.weather.engine.City;
import cn.kli.weather.engine.RequestResult;
import cn.kli.weather.engine.WeatherSource;

public class SourceWeatherComCn implements WeatherSource, RequestResult {
	private final static String WEATHER_URL = "http://m.weather.com.cn/data/xxx.html";
    private final static String WEATHER_SK_URL = "http://www.weather.com.cn/data/sk/xxx.html";
	private final static String CITY_URL = "http://m.weather.com.cn/data5/cityxxx.xml";
	private final static String SOURCE = "weather.com.cn";
	
	private static boolean mIniting = false;
	private Context mContext;
	private DataProxy mDataProxy;
	private InternetAccess mAccess;
	
	public SourceWeatherComCn(Context context){
		mContext = context;
	}
	
	@Override
	public String getSourceName() {
		return SOURCE;
	}

	@Override
	public int onInit() {
		mDataProxy = DataProxy.getInstance(mContext);
		mAccess = InternetAccess.getInstance(mContext);
		
		klilog.info("prepared = "+mDataProxy.getDataPrepared()+", mIniting = "+mIniting);
		if(!mDataProxy.getDataPrepared() &&!mIniting){
			mIniting = true;
			try {
				ArrayList<MyCity> cities = getCitiesByIndex("");
				saveCities(cities);
				mDataProxy.setDataPrepared(true);
			} catch (Exception e) {
				return RES_ERROR_UNKOWN;
			}
		}else{
			
		}
		return RES_SUCCESS;
	}

	private void saveCities(ArrayList<MyCity> cities){
		for(MyCity city : cities){
			mDataProxy.addCity(city);
			if(city.level <= 2){
				ArrayList<MyCity> childCities = getCitiesByIndex(city.index);
				if(city.level == 2){
					checkCodeByCity(childCities);
				}
				saveCities(childCities);
			}
		}
	}
	
	private ArrayList<MyCity> getCitiesByIndex(String index){
		if(index == null){
			index = "";
		}
		String url = CITY_URL.replace("xxx", index);
		ArrayList<MyCity> cities = new ArrayList<MyCity>();
		String response = mAccess.request(url);
		klilog.info("response = "+response);
		if(!TextUtils.isEmpty(response)){
			cities = CityParser.parser(response);
		}else{
			klilog.info("error respose null");
			throw new NullPointerException();
		}
		return cities;
	}
	
	private void checkCodeByCity(ArrayList<MyCity> cities){
		if(cities == null){
			return;
		}
		for(MyCity city : cities){
			String url = CITY_URL.replace("xxx",city.index);
			String response = mAccess.request(url);
			klilog.info("response = "+response);
			if(!TextUtils.isEmpty(response)){
				city.code = response.split("\\|")[1];
			}else{
				klilog.info("error respose null");
				throw new NullPointerException();
			}
		}
	}

	/**
	 * Must be called in thread.
	 */
	@Override
	public City getWeatherByIndex(String index) {
		MyCity city = mDataProxy.getCityByIndex(index);
		String url = WEATHER_URL.replace("xxx", city.code);
		String response = mAccess.request(url);
		klilog.info("response = "+response);
		if(!TextUtils.isEmpty(response)){
			city = WeatherParser.parser(city, response);
			klilog.info("finish");
		}else{
			klilog.info("error respose null");
			throw new NullPointerException();
		}
        String skUrl = WEATHER_SK_URL.replace("xxx", city.code);
        String skResponse = mAccess.request(skUrl);
        city.weather.get(0).currentTemp = WeatherParser.getCurrentTemp(skResponse);
		return city;
	}

	@Override
	public List<City> getCityList(City city) {
		return mDataProxy.getCityList(city);
	}

    @Override
    public List<City> searchCityByName(String name) {
        return null;
    }
}
