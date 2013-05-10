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
	
	/*��������ʵ�������£�20�棻����/���������� 4����ʪ�ȣ�29%*/
	private final static int WEATHER_DETAIL = 4;
	private final static int AIR = 5;
	private final static int MORE_INFO = 6;
	
	/*3��27�� ��ת����*/
	private final static int DAY_1 = 7;
	/*6��/20��*/
	private final static int DAY_1_TEMP = 8;
	/*���Ϸ�3-4��*/
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
					int p_end = details.indexOf("��");
					int p_start = details.indexOf("���£�") + "���£�".length();
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
				weather.minTemp = temp[0].substring(0, temp[0].indexOf("��"));
				weather.maxTemp = temp[1].substring(0, temp[1].indexOf("��"));

				// weather;
				String weatherDay = infos.get(DAYS_WEATHER[i][0]);
				weather.weather = getWeatherByName(weatherDay.split(" ")[1]);

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
		String[] names = name.split("ת");
		ArrayList<Integer> nameList = new ArrayList<Integer>();
		
		for(String item : names){
			int w = Weather.W_NO_DATA;
			if("��".equals(item)){
				w = Weather.W_QING;
			}else if("����".equals(item)){
				w = Weather.W_DUOYUN;
			}else if("��".equals(item)){
				w = Weather.W_YIN;
			}else if("����".equals(item)){
				w = Weather.W_ZHENYU;
			}else if("������".equals(item)){
				w = Weather.W_LEIZHENYU;
			}else if("�����겢���б���".equals(item)){
				w = Weather.W_LEIZHENYUBINGBANYOUBINGBAO;
			}else if("���ѩ".equals(item)){
				w = Weather.W_YUJIAXUE;
			}else if("С��".equals(item)){
				w = Weather.W_XIAOYU;
			}else if("����".equals(item)){
				w = Weather.W_ZHONGYU;
			}else if("����".equals(item)){
				w = Weather.W_DAYU;
			}else if("����".equals(item)){
				w = Weather.W_BAOYU;
			}else if("����".equals(item)){
				w = Weather.W_DABAOYU;
			}else if("�ش���".equals(item)){
				w = Weather.W_TEDABAOYU;
			}else if("��ѩ".equals(item)){
				w = Weather.W_ZHENXUE;
			}else if("Сѩ".equals(item)){
				w = Weather.W_XIAOXUE;
			}else if("��ѩ".equals(item)){
				w = Weather.W_ZHONGXUE;
			}else if("��ѩ".equals(item)){
				w = Weather.W_DAXUE;
			}else if("��ѩ".equals(item)){
				w = Weather.W_BAOXUE;
			}else if("��".equals(item)){
				w = Weather.W_WU;
			}else if("����".equals(item)){
				w = Weather.W_DONGYU;
			}else if("ɳ����".equals(item)){
				w = Weather.W_SHACHENBAO;
			}else if("С��-����".equals(item)){
				w = Weather.W_XIAOYUZHONGYU;
			}else if("����-����".equals(item)){
				w = Weather.W_ZHONGYUDAYU;
			}else if("����-����".equals(item)){
				w = Weather.W_DAYUBAOYU;
			}else if("����-����".equals(item)){
				w = Weather.W_BAOYUDABAOYU;
			}else if("����-�ش���".equals(item)){
				w = Weather.W_DABAOYUTEDABAOYU;
			}else if("Сѩ-��ѩ".equals(item)){
				w = Weather.W_XIAOXUEZHONGXUE;
			}else if("��ѩ-��ѩ".equals(item)){
				w = Weather.W_ZHONGXUEDAXUE;
			}else if("��ѩ-��ѩ".equals(item)){
				w = Weather.W_DAXUEBAOXUE;
			}else if("����".equals(item)){
				w = Weather.W_FUCHEN;
			}else if("��ɳ".equals(item)){
				w = Weather.W_YANGSHA;
			}else if("ǿɳ����".equals(item)){
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
