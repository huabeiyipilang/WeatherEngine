package weathersource.weather.com.cn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.kli.weather.engine.Weather;
import cn.kli.weatherengine.R;

public class WeatherParser {
	
	public static MyCity parser(MyCity city, String source){
		try {
			JSONObject jsonObject = new JSONObject(source).getJSONObject("weatherinfo");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍMM‘¬dd»’");
			String date_y = jsonObject.getString("date_y");
			Calendar calendar = null;
			try {
				Date date = sdf.parse(date_y);
				calendar = new GregorianCalendar();
				calendar.setTime(date);
			} catch (ParseException e) {
				return null;
			}

			ArrayList<Weather> weathers = new ArrayList<Weather>();
			//get 'days' weather from today
			int days = 3;
			for(int i = 1; i <= days; i++){
				Weather weather = new Weather();
				Calendar cal = (Calendar) calendar.clone();
				cal.add(Calendar.DAY_OF_MONTH, i - 1);
				weather.calendar = cal;
				if(i == 1){
					weather.currentTemp = jsonObject.getString("fchh");
				}
				String[] tempRange = jsonObject.getString("temp"+i).split("~");
				weather.maxTemp = tempRange[0];
				weather.minTemp = tempRange[1];
				weather.weather = getWeatherByName(jsonObject.getString("weather"+i));
				weather.wind = jsonObject.getString("wind"+i);
				weathers.add(weather);
			}
			
			city.weather = weathers;
		} catch (JSONException e) {
			city = null;
			e.printStackTrace();
		}
		
		return city;
	}
	


	private static int[] getWeatherByName(String name){
		String[] names = name.split("◊™");
		ArrayList<Integer> nameList = new ArrayList<Integer>();
		
		for(String item : names){
			int w = Weather.W_NO_DATA;
			if("«Á".equals(item)){
				w = Weather.W_QING;
			}else if("∂‡‘∆".equals(item)){
				w = Weather.W_DUOYUN;
			}else if("“ı".equals(item)){
				w = Weather.W_YIN;
			}else if("’Û”Í".equals(item)){
				w = Weather.W_ZHENYU;
			}else if("¿◊’Û”Í".equals(item)){
				w = Weather.W_LEIZHENYU;
			}else if("¿◊’Û”Í≤¢∞È”–±˘±¢".equals(item)){
				w = Weather.W_LEIZHENYUBINGBANYOUBINGBAO;
			}else if("”Íº–—©".equals(item)){
				w = Weather.W_YUJIAXUE;
			}else if("–°”Í".equals(item)){
				w = Weather.W_XIAOYU;
			}else if("÷–”Í".equals(item)){
				w = Weather.W_ZHONGYU;
			}else if("¥Û”Í".equals(item)){
				w = Weather.W_DAYU;
			}else if("±©”Í".equals(item)){
				w = Weather.W_BAOYU;
			}else if("¥Û±©”Í".equals(item)){
				w = Weather.W_DABAOYU;
			}else if("Ãÿ¥Û±©”Í".equals(item)){
				w = Weather.W_TEDABAOYU;
			}else if("’Û—©".equals(item)){
				w = Weather.W_ZHENXUE;
			}else if("–°—©".equals(item)){
				w = Weather.W_XIAOXUE;
			}else if("÷–—©".equals(item)){
				w = Weather.W_ZHONGXUE;
			}else if("¥Û—©".equals(item)){
				w = Weather.W_DAXUE;
			}else if("±©—©".equals(item)){
				w = Weather.W_BAOXUE;
			}else if("ŒÌ".equals(item)){
				w = Weather.W_WU;
			}else if("∂≥”Í".equals(item)){
				w = Weather.W_DONGYU;
			}else if("…≥≥æ±©".equals(item)){
				w = Weather.W_SHACHENBAO;
			}else if("–°”Í-÷–”Í".equals(item)){
				w = Weather.W_XIAOYUZHONGYU;
			}else if("÷–”Í-¥Û”Í".equals(item)){
				w = Weather.W_ZHONGYUDAYU;
			}else if("¥Û”Í-±©”Í".equals(item)){
				w = Weather.W_DAYUBAOYU;
			}else if("±©”Í-¥Û±©”Í".equals(item)){
				w = Weather.W_BAOYUDABAOYU;
			}else if("¥Û±©”Í-Ãÿ¥Û±©”Í".equals(item)){
				w = Weather.W_DABAOYUTEDABAOYU;
			}else if("–°—©-÷–—©".equals(item)){
				w = Weather.W_XIAOXUEZHONGXUE;
			}else if("÷–—©-¥Û—©".equals(item)){
				w = Weather.W_ZHONGXUEDAXUE;
			}else if("¥Û—©-±©—©".equals(item)){
				w = Weather.W_DAXUEBAOXUE;
			}else if("∏°≥æ".equals(item)){
				w = Weather.W_FUCHEN;
			}else if("—Ô…≥".equals(item)){
				w = Weather.W_YANGSHA;
			}else if("«ø…≥≥æ±©".equals(item)){
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