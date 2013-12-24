package cn.kli.weather.engine;

import cn.kli.weather.engine.Weather;
import cn.kli.weatherengine.R;
import android.content.Context;

public class WeatherUtils {

	public static int getDrawable(int[] weather) {
		int res = R.drawable.a_nothing;
		try {
			int weatherId = weather[0];
			res = getDrawable(weatherId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static int getDrawable(int weather) {
		int res = R.drawable.a_nothing;
		switch(weather){
		case Weather.W_QING:
			res = R.drawable.a_0;
			break;
		case Weather.W_DUOYUN:
			res = R.drawable.a_1;
			break;
        case Weather.W_MAI:
		case Weather.W_YIN:
			res = R.drawable.a_2;
			break;
		case Weather.W_ZHENYU:
			res = R.drawable.a_3;
			break;
		case Weather.W_LEIZHENYU:
			res = R.drawable.a_4;
			break;
		case Weather.W_LEIZHENYUBINGBANYOUBINGBAO:
			res = R.drawable.a_5;
			break;
		case Weather.W_YUJIAXUE:
			res = R.drawable.a_6;
			break;
		case Weather.W_XIAOYU:
			res = R.drawable.a_7;
			break;
		case Weather.W_ZHONGYU:
			res = R.drawable.a_8;
			break;
		case Weather.W_DAYU:
			res = R.drawable.a_9;
			break;
		case Weather.W_BAOYU:
			res = R.drawable.a_10;
			break;
		case Weather.W_DABAOYU:
			res = R.drawable.a_11;
			break;
		case Weather.W_TEDABAOYU:
			res = R.drawable.a_12;
			break;
		case Weather.W_ZHENXUE:
			res = R.drawable.a_13;
			break;
		case Weather.W_XIAOXUE:
			res = R.drawable.a_14;
			break;
		case Weather.W_ZHONGXUE:
			res = R.drawable.a_15;
			break;
		case Weather.W_DAXUE:
			res = R.drawable.a_16;
			break;
		case Weather.W_BAOXUE:
			res = R.drawable.a_17;
			break;
		case Weather.W_WU:
			res = R.drawable.a_18;
			break;
		case Weather.W_DONGYU:
			res = R.drawable.a_19;
			break;
		case Weather.W_SHACHENBAO:
			res = R.drawable.a_20;
			break;
		case Weather.W_XIAOYUZHONGYU:
			res = R.drawable.a_21;
			break;
		case Weather.W_ZHONGYUDAYU:
			res = R.drawable.a_22;
			break;
		case Weather.W_DAYUBAOYU:
			res = R.drawable.a_23;
			break;
		case Weather.W_BAOYUDABAOYU:
			res = R.drawable.a_24;
			break;
		case Weather.W_DABAOYUTEDABAOYU:
			res = R.drawable.a_25;
			break;
		case Weather.W_XIAOXUEZHONGXUE:
			res = R.drawable.a_26;
			break;
		case Weather.W_ZHONGXUEDAXUE:
			res = R.drawable.a_27;
			break;
		case Weather.W_DAXUEBAOXUE:
			res = R.drawable.a_28;
			break;
		case Weather.W_FUCHEN:
			res = R.drawable.a_29;
			break;
		case Weather.W_YANGSHA:
			res = R.drawable.a_30;
			break;
		case Weather.W_QIANGSHACHENBAO:
			res = R.drawable.a_31;
			break;
		}
		return res;
	}

	public static String getWeather(Context context, int[] weather){
		String res = context.getString(R.string.error);
		if(weather.length == 2){
			if(weather[0] == weather[1]){
				res = context.getString(getName(weather[0]));
			}else{
				res = context.getString(getName(weather[0]))
						+ context.getString(R.string.weather_to)
						+ context.getString(getName(weather[1]));
			}
		}else{
			res = context.getString(getName(weather[0]));
		}
		return res;
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
	
	private static String getName(Context context, int weather){
		return context.getString(getName(weather));
	}
	
	public static int getName(int weather){
		int res = R.string.weather_nothing;
		switch(weather){
		case Weather.W_QING:
			res = R.string.weather_q;
			break;
		case Weather.W_DUOYUN:
			res = R.string.weather_dyun;
			break;
		case Weather.W_YIN:
			res = R.string.weather_y;
			break;
		case Weather.W_ZHENYU:
			res = R.string.weather_zhy;
			break;
		case Weather.W_LEIZHENYU:
			res = R.string.weather_lzhy;
			break;
		case Weather.W_LEIZHENYUBINGBANYOUBINGBAO:
			res = R.string.weather_lzhybbybb;
			break;
		case Weather.W_YUJIAXUE:
			res = R.string.weather_yjx;
			break;
		case Weather.W_XIAOYU:
			res = R.string.weather_xy;
			break;
		case Weather.W_ZHONGYU:
			res = R.string.weather_zhy;
			break;
		case Weather.W_DAYU:
			res = R.string.weather_dy;
			break;
		case Weather.W_BAOYU:
			res = R.string.weather_by;
			break;
		case Weather.W_DABAOYU:
			res = R.string.weather_dby;
			break;
		case Weather.W_TEDABAOYU:
			res = R.string.weather_tdby;
			break;
		case Weather.W_ZHENXUE:
			res = R.string.weather_zhx;
			break;
		case Weather.W_XIAOXUE:
			res = R.string.weather_xx;
			break;
		case Weather.W_ZHONGXUE:
			res = R.string.weather_zhongx;
			break;
		case Weather.W_DAXUE:
			res = R.string.weather_dx;
			break;
		case Weather.W_BAOXUE:
			res = R.string.weather_bx;
			break;
		case Weather.W_WU:
			res = R.string.weather_w;
			break;
		case Weather.W_DONGYU:
			res = R.string.weather_dy;
			break;
		case Weather.W_SHACHENBAO:
			res = R.string.weather_schb;
			break;
		case Weather.W_XIAOYUZHONGYU:
			res = R.string.weather_xyzhy;
			break;
		case Weather.W_ZHONGYUDAYU:
			res = R.string.weather_zhydy;
			break;
		case Weather.W_DAYUBAOYU:
			res = R.string.weather_dyby;
			break;
		case Weather.W_BAOYUDABAOYU:
			res = R.string.weather_bydby;
			break;
		case Weather.W_DABAOYUTEDABAOYU:
			res = R.string.weather_dbytdby;
			break;
		case Weather.W_XIAOXUEZHONGXUE:
			res = R.string.weather_xxzhx;
			break;
		case Weather.W_ZHONGXUEDAXUE:
			res = R.string.weather_zhxdx;
			break;
		case Weather.W_DAXUEBAOXUE:
			res = R.string.weather_dxbx;
			break;
		case Weather.W_FUCHEN:
			res = R.string.weather_fch;
			break;
		case Weather.W_YANGSHA:
			res = R.string.weather_ysh;
			break;
		case Weather.W_QIANGSHACHENBAO:
			res = R.string.weather_qshchb;
			break;
		case Weather.W_MAI:
		    res = R.string.weather_mai;
		    break;
		}
		return res;
	}
	
}
