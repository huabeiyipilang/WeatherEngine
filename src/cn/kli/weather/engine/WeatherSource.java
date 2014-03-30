package cn.kli.weather.engine;

import java.util.List;

/**
 * 天气源接口
 * @Package cn.kli.weather.engine
 * @ClassName: WeatherSource
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-3-30 上午9:48:14
 */
public interface WeatherSource {
	String getSourceName();
	int onInit();
	City getWeatherByCityIndex(String index);
	List<City> getCityList(City city);
	List<City> searchCityByName(String name);
}
