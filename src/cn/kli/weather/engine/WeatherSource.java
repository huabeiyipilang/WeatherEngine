package cn.kli.weather.engine;

import java.util.List;


/**
 * 天气源父类
 * @Package cn.kli.weather.engine
 * @ClassName: WeatherSource
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-4-4 下午3:47:37
 */
public class WeatherSource {
    public final static int NOT_SUPPORT = -1;
    
	/**
	 * 返回天气源
	 * @Title: getSourceName
	 * @return
	 * @return String
	 * @date 2014-4-4 下午3:48:01
	 */
	protected String getSourceName(){
	    return null;
	}
	
	/**
	 * 初始化
	 * @Title: onInit
	 * @return
	 * @return int
	 * @date 2014-4-4 下午3:50:19
	 */
	protected int onInit(){
        return ErrorCode.NOT_SUPPORT;
    }
	
	/**
	 * 查询天气
	 * @Title: requestWeatherByCity
	 * @param city
	 * @return
	 * @return int
	 * @date 2014-4-4 下午3:50:36
	 */
	protected int requestWeatherByCity(City city){
        return ErrorCode.NOT_SUPPORT;
    }
	
	/**
	 * 查询子子城市列表
	 * @Title: requestCityList
	 * @param city null，表示根目录
	 * @param responseList
	 * @return
	 * @return int
	 * @date 2014-4-4 下午3:50:46
	 */
	protected int requestCityList(City city, List<City> responseList){
        return ErrorCode.NOT_SUPPORT;
    }
	
	/**
	 * 根据名称查询城市
	 * @Title: searchCityByName
	 * @param name
	 * @param responseList
	 * @return
	 * @return int
	 * @date 2014-4-4 下午3:52:00
	 */
	protected int searchCityByName(String name, List<City> responseList){
        return ErrorCode.NOT_SUPPORT;
    }
}
