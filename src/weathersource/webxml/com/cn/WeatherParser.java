package weathersource.webxml.com.cn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;

import cn.kli.weather.engine.City;
import cn.kli.weather.engine.Weather;

public class WeatherParser {
	
	private final static int PARENT = 0;
	private final static int NAME = 1;
	private final static int INDEX = 2;
	
	/*2013/03/27 16:40:41*/
	private final static int NOW = 3;
	
	/*今日天气实况：气温：20℃；风向/风力：西风 4级；湿度：29%*/
	private final static int WEATHER_DETAIL = 4;
	private final static int AIR = 5;
	private final static int MORE_INFO = 6;
	
	/*3月27日 晴转多云*/
	private final static int DAY_1 = 7;
	/*6℃/20℃*/
	private final static int DAY_1_TEMP = 8;
	/*西南风3-4级*/
	private final static int DAY_1_WIND = 9;
	private final static int DAY_1_PIC1 = 10;
	private final static int DAY_1_PIC2 = 11;

	private final static int DAY_2 = 12;
	private final static int DAY_2_TEMP = 13;
	private final static int DAY_2_WIND = 14;
	private final static int DAY_2_PIC1 = 15;
	private final static int DAY_2_PIC2 = 16;
	
	private final static int DAY_3 = 17;
	private final static int DAY_3_TEMP = 18;
	private final static int DAY_3_WIND = 19;
	private final static int DAY_3_PIC1 = 20;
	private final static int DAY_3_PIC2 = 21;

	private final static int DAY_4 = 22;
	private final static int DAY_4_TEMP = 23;
	private final static int DAY_4_WIND = 24;
	private final static int DAY_4_PIC1 = 25;
	private final static int DAY_4_PIC2 = 26;

	private final static int DAY_5 = 27;
	private final static int DAY_5_TEMP = 28;
	private final static int DAY_5_WIND = 29;
	private final static int DAY_5_PIC1 = 30;
	private final static int DAY_5_PIC2 = 31;
	
	private final static int[][] DAYS_WEATHER = {{DAY_1, DAY_1_TEMP, DAY_1_WIND, DAY_1_PIC1, DAY_1_PIC2},
		{DAY_2, DAY_2_TEMP, DAY_2_WIND, DAY_2_PIC1, DAY_2_PIC2}, {DAY_3, DAY_3_TEMP, DAY_3_WIND, DAY_3_PIC1, DAY_3_PIC2}, 
		{DAY_4, DAY_4_TEMP, DAY_4_WIND, DAY_4_PIC1, DAY_4_PIC2}, {DAY_5, DAY_5_TEMP, DAY_5_WIND, DAY_5_PIC1, DAY_5_PIC2}};
	
	public static MyCity parser(MyCity city, String source){

		try {
			List<String> infos = new StringArrayXmlParser(source).parser();

			// get days weather, include today
			int days = 4;
			ArrayList<Weather> weatherList = new ArrayList<Weather>();

			// get the first day calendar
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String dateToday = infos.get(NOW).split(" ")[0];
			Calendar calendar = null;
			Date date = sdf.parse(dateToday);
			calendar = new GregorianCalendar();
			calendar.setTime(date);

			for (int i = 0; i < days; i++) {
				Weather weather = new Weather();

				// calendar
				Calendar cal = (Calendar) calendar.clone();
				cal.add(Calendar.DAY_OF_MONTH, i);
				weather.calendar = cal;

				// currentTemp
				if (i == 0) {
					String details = infos.get(WEATHER_DETAIL);
					int p_end = details.indexOf("℃");
					int p_start = details.indexOf("气温：") + "气温：".length();
					try {
						weather.currentTemp = details.substring(p_start, p_end);
					} catch (Exception e) {
						weather.currentTemp = "??";
						e.printStackTrace();
					}
				}

				// maxTemp;
				// minTemp;
				String temps = infos.get(DAYS_WEATHER[i][1]);
				String[] temp = temps.split("/");
				weather.minTemp = temp[0].substring(0, temp[0].indexOf("℃"));
				weather.maxTemp = temp[1].substring(0, temp[1].indexOf("℃"));

				// weather;
				String weatherDay = infos.get(DAYS_WEATHER[i][0]);
				String weatherDetails = weatherDay.split(" ")[1];
				weather.weather = getWeatherByName(weatherDetails);

				// weather pic
//				String pic1 = infos.get(DAYS_WEATHER[i][3]).split("\\.")[0];
//				String pic2 = infos.get(DAYS_WEATHER[i][4]).split("\\.")[0];
//				weather.weather = pic1 + "," + pic2;

				// wind;
				weather.wind = infos.get(DAYS_WEATHER[i][2]);

				weatherList.add(weather);
			}

			city.weather = weatherList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return city;
	}
	

	private static int[] getWeatherByName(String name){
		String[] names = name.split("转");
		ArrayList<Integer> nameList = new ArrayList<Integer>();
		
		for(String item : names){
			int w = Weather.W_NO_DATA;
			if("晴".equals(item)){
				w = Weather.W_QING;
			}else if("多云".equals(item)){
				w = Weather.W_DUOYUN;
			}else if("阴".equals(item)){
				w = Weather.W_YIN;
			}else if("阵雨".equals(item)){
				w = Weather.W_ZHENYU;
			}else if("雷阵雨".equals(item)){
				w = Weather.W_LEIZHENYU;
			}else if("雷阵雨并伴有冰雹".equals(item)){
				w = Weather.W_LEIZHENYUBINGBANYOUBINGBAO;
			}else if("雨夹雪".equals(item)){
				w = Weather.W_YUJIAXUE;
			}else if("小雨".equals(item)){
				w = Weather.W_XIAOYU;
			}else if("中雨".equals(item)){
				w = Weather.W_ZHONGYU;
			}else if("大雨".equals(item)){
				w = Weather.W_DAYU;
			}else if("暴雨".equals(item)){
				w = Weather.W_BAOYU;
			}else if("大暴雨".equals(item)){
				w = Weather.W_DABAOYU;
			}else if("特大暴雨".equals(item)){
				w = Weather.W_TEDABAOYU;
			}else if("阵雪".equals(item)){
				w = Weather.W_ZHENXUE;
			}else if("小雪".equals(item)){
				w = Weather.W_XIAOXUE;
			}else if("中雪".equals(item)){
				w = Weather.W_ZHONGXUE;
			}else if("大雪".equals(item)){
				w = Weather.W_DAXUE;
			}else if("暴雪".equals(item)){
				w = Weather.W_BAOXUE;
			}else if("雾".equals(item)){
				w = Weather.W_WU;
			}else if("冻雨".equals(item)){
				w = Weather.W_DONGYU;
			}else if("沙尘暴".equals(item)){
				w = Weather.W_SHACHENBAO;
			}else if("小雨-中雨".equals(item)){
				w = Weather.W_XIAOYUZHONGYU;
			}else if("中雨-大雨".equals(item)){
				w = Weather.W_ZHONGYUDAYU;
			}else if("大雨-暴雨".equals(item)){
				w = Weather.W_DAYUBAOYU;
			}else if("暴雨-大暴雨".equals(item)){
				w = Weather.W_BAOYUDABAOYU;
			}else if("大暴雨-特大暴雨".equals(item)){
				w = Weather.W_DABAOYUTEDABAOYU;
			}else if("小雪-中雪".equals(item)){
				w = Weather.W_XIAOXUEZHONGXUE;
			}else if("中雪-大雪".equals(item)){
				w = Weather.W_ZHONGXUEDAXUE;
			}else if("大雪-暴雪".equals(item)){
				w = Weather.W_DAXUEBAOXUE;
			}else if("浮尘".equals(item)){
				w = Weather.W_FUCHEN;
			}else if("扬沙".equals(item)){
				w = Weather.W_YANGSHA;
			}else if("强沙尘暴".equals(item)){
				w = Weather.W_QIANGSHACHENBAO;
			}
			nameList.add(w);
		}
		int[] res = null;
		if(nameList.size() > 0){
			res = listToPrimitive(nameList);
		}
		
		return res;
	}
	
	private static int[] listToPrimitive(List<Integer> list) {
	    if (list == null) {
	        return null;
	    }
	    int[] result = new int[list.size()];
	    for (int i = 0; i < list.size(); ++i) {
	        result[i] = list.get(i).intValue();
	    }
	    return result;
	}
}
