package cn.kli.weather.engine;

import java.util.List;

public interface WeatherSource {
	String getSourceName();
	int onInit();
	City getWeatherByIndex(String index);
	List<City> getCityList(City city);
	List<City> searchCityByName(String name);
}
