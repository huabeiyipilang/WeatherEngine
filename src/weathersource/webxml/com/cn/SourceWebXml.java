package weathersource.webxml.com.cn;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import cn.kli.utils.klilog;
import cn.kli.weather.engine.City;
import cn.kli.weather.engine.RequestResult;
import cn.kli.weather.engine.WeatherEngine;
import cn.kli.weather.engine.WeatherSource;

public class SourceWebXml implements WeatherSource {
	private static final String PROVINCE_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getRegionProvince";
	private static final String CITY_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getSupportCityString?theRegionCode=";
	private static final String WEATHER_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";

	private final static String SOURCE = "WebXml";

	private static boolean mIniting = false;
	private Context mContext;
	private DataProxy mDataProxy;
	private InternetAccess mAccess;

	public SourceWebXml(Context context){
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
				ArrayList<MyCity> provinces = getProvinceList();
				for(MyCity province : provinces){
					mDataProxy.addCity(province);
					saveCityList(province.index);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return RequestResult.RES_ERROR_UNKOWN;
			}
			mDataProxy.setDataPrepared(true);
		}
		return RequestResult.RES_SUCCESS;
	}
	
	private ArrayList<MyCity> getProvinceList(){
		ArrayList<MyCity> list = new ArrayList<MyCity>();
		String response = mAccess.request(PROVINCE_URL);
		klilog.info("response = "+response);
		if(!TextUtils.isEmpty(response)){
			list = CityParser.parser(response);
		}else{
			klilog.info("error respose null");
			throw new NullPointerException();
		}
		return list;
	}
	
	private ArrayList<MyCity> saveCityList(String index){
		ArrayList<MyCity> list = new ArrayList<MyCity>();
		String response = mAccess.request(CITY_URL+index);
		klilog.info("response = "+response);
		if(!TextUtils.isEmpty(response)){
			list = CityParser.parser(response, index);
			for(MyCity city : list){
				mDataProxy.addCity(city);
			}
		}else{
			klilog.info("error respose null");
			throw new NullPointerException();
		}
		return list;
	}

	@Override
	public City getWeatherByIndex(String index) {
		MyCity city = mDataProxy.getCityByIndex(index);
		String response = mAccess.request(getWeatherUrl(index));
		if(!TextUtils.isEmpty(response)){
			klilog.info("response = "+response);
			WeatherParser.parser(city, response);
		}else{
			klilog.info("error respose null");
		}
		return city;
	}
	
	private String getWeatherUrl(String index){
		return WEATHER_URL + "?theCityCode=" + index + "&theUserId=";
	}

	@Override
	public List<City> getCityList(City city) {
		return mDataProxy.getCityList(city);
	}

}
