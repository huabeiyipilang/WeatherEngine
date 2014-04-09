package cn.kli.weather.engine;

import android.content.Context;
import cn.kli.weatherengine.R;

class WeatherUtils {

	public static String[] getWeather(int[] weather){
		String[] weatherArray = new String[weather.length];
		for(int i = 0; i < weather.length; i++){
		    weatherArray[i] = getName(weather[i]);
		}
		return weatherArray;
	}
	
	public static boolean isBadWeather(Weather weather) {
		boolean res = false;
		if (weather != null && weather.weather != null) {
			switch (weather.weather.length) {
			case 1:
				res = isBadWeather(weather.weather[0]);
				break;
			case 2:
				int weatherFrom = weather.weather[0];
				int weatherTo = weather.weather[1];
				res = (weatherFrom == weatherTo) ? isBadWeather(weatherFrom)
						: isBadWeather(weatherFrom) | isBadWeather(weatherTo);
				break;
			}
		}
		return res;
	}
	
	private static boolean isBadWeather(int weather){
		boolean res = true;
		switch(weather){
		case Weather.W_QING:
		case Weather.W_DUOYUN:
		case Weather.W_YIN:
		case Weather.W_WU:
			res = false;
			break;
		}
		return res;
	}
	
	
	public static String getName(int weather){
		String res = "N/A";
		switch(weather){
		case Weather.W_QING:
			res = "晴";
			break;
		case Weather.W_DUOYUN:
            res = "多云";
			break;
		case Weather.W_YIN:
            res = "阴";
			break;
		case Weather.W_ZHENYU:
            res = "阵雨";
			break;
		case Weather.W_LEIZHENYU:
            res = "雷阵雨";
			break;
		case Weather.W_LEIZHENYUBINGBANYOUBINGBAO:
            res = "雷阵雨并伴有冰雹";
			break;
		case Weather.W_YUJIAXUE:
            res = "雨加雪";
			break;
		case Weather.W_XIAOYU:
            res = "小雨";
			break;
		case Weather.W_ZHONGYU:
            res = "中雨";
			break;
		case Weather.W_DAYU:
            res = "大雨";
			break;
		case Weather.W_BAOYU:
            res = "暴雨";
			break;
		case Weather.W_DABAOYU:
            res = "大暴雨";
			break;
		case Weather.W_TEDABAOYU:
            res = "特大暴雨";
			break;
		case Weather.W_ZHENXUE:
            res = "阵雪";
			break;
		case Weather.W_XIAOXUE:
            res = "小雪";
			break;
		case Weather.W_ZHONGXUE:
            res = "中雪";
			break;
		case Weather.W_DAXUE:
            res = "大雪";
			break;
		case Weather.W_BAOXUE:
            res = "暴雪";
			break;
		case Weather.W_WU:
            res = "雾";
			break;
		case Weather.W_DONGYU:
            res = "冻雨";
			break;
		case Weather.W_SHACHENBAO:
            res = "沙尘暴";
			break;
		case Weather.W_XIAOYUZHONGYU:
            res = "小雨-中雨";
			break;
		case Weather.W_ZHONGYUDAYU:
            res = "中雨-大雨";
			break;
		case Weather.W_DAYUBAOYU:
            res = "大雨-暴雨";
			break;
		case Weather.W_BAOYUDABAOYU:
            res = "暴雨-大暴雨";
			break;
		case Weather.W_DABAOYUTEDABAOYU:
            res = "大暴雨-特大暴雨";
			break;
		case Weather.W_XIAOXUEZHONGXUE:
            res = "小雪-中雪";
			break;
		case Weather.W_ZHONGXUEDAXUE:
            res = "中雪-大雪";
			break;
		case Weather.W_DAXUEBAOXUE:
            res = "大雪-暴雪";
			break;
		case Weather.W_FUCHEN:
            res = "浮尘";
			break;
		case Weather.W_YANGSHA:
            res = "扬沙";
			break;
		case Weather.W_QIANGSHACHENBAO:
            res = "强沙尘暴";
			break;
		case Weather.W_MAI:
            res = "霾";
		    break;
		}
		return res;
	}
	
}
