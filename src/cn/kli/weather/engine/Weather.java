package cn.kli.weather.engine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import cn.kli.utils.dao.BaseInfo;
import cn.kli.utils.dao.DbField;
import cn.kli.utils.dao.DbField.DataType;
import cn.kli.weatherengine.R;

/**
 * 天气类
 * @Package cn.kli.weather.engine
 * @ClassName: Weather
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-3-29 下午3:04:55
 */
public class Weather extends BaseInfo implements Parcelable{
	
	/**
	 * 无数据
	 */
	public final static int W_NO_DATA = 0;

	/**
	 * 晴
	 */
	public final static int W_QING = 1;

	/**
	 * 多云
	 */
	public final static int W_DUOYUN = 2;

	/**
	 * 阴
	 */
	public final static int W_YIN = 3;

	/**
	 * 阵雨
	 */
	public final static int W_ZHENYU = 4;

	/**
	 * 雷阵雨
	 */
	public final static int W_LEIZHENYU = 5;

	/**
	 * 雷阵雨并伴有冰雹
	 */
	public final static int W_LEIZHENYUBINGBANYOUBINGBAO = 6;
	
	/**
	 * 雨夹雪
	 */
	public final static int W_YUJIAXUE = 7;

	/**
	 * 小雨
	 */
	public final static int W_XIAOYU = 8;

	/**
	 * 中雨
	 */
	public final static int W_ZHONGYU = 9;
	/**
	 * 大雨
	 */
	public final static int W_DAYU = 10;
	/**
	 * 暴雨
	 */
	public final static int W_BAOYU = 11;
	/**
	 * 大暴雨
	 */
	public final static int W_DABAOYU = 12;
	/**
	 * 特大暴雨
	 */
	public final static int W_TEDABAOYU = 13;
	/**
	 * 阵雪
	 */
	public final static int W_ZHENXUE = 14;
	/**
	 * 小雪
	 */
	public final static int W_XIAOXUE = 15;
	/**
	 * 中雪
	 */
	public final static int W_ZHONGXUE = 16;
	/**
	 * 大雪
	 */
	public final static int W_DAXUE = 17;
	/**
	 * 暴雪
	 */
	public final static int W_BAOXUE = 18;
	/**
	 * 雾
	 */
	public final static int W_WU = 19;
	/**
	 * 冻雨
	 */
	public final static int W_DONGYU = 20;
	/**
	 * 沙尘暴
	 */
	public final static int W_SHACHENBAO = 21;

	public final static int W_XIAOYUZHONGYU = 22;
	public final static int W_ZHONGYUDAYU = 23;
	public final static int W_DAYUBAOYU = 24;
	public final static int W_BAOYUDABAOYU = 25;
	public final static int W_DABAOYUTEDABAOYU = 26;
	public final static int W_XIAOXUEZHONGXUE = 27;
	public final static int W_ZHONGXUEDAXUE = 28;
	public final static int W_DAXUEBAOXUE = 29;
	
	/**
	 * 浮尘
	 */
	public final static int W_FUCHEN = 30;
	/**
	 * 扬沙
	 */
	public final static int W_YANGSHA = 31;
	public final static int W_QIANGSHACHENBAO = 32;
	/**
	 * 霾
	 */
	public final static int W_MAI = 33;
	
	
	
	/**
	 * 城市id
	 */
    @DbField(name = "city_index", type = DataType.TEXT, isNull = false)
	public String city_index;
	/**
	 * 日期
	 */
    @DbField(name = "calendar", type = DataType.CALENDAR, isNull = false)
	public Calendar calendar;
	
	/**
	 * 当前温度（只对当天有效）
	 */
    @DbField(name = "current_temp", type = DataType.TEXT)
	public String currentTemp;
	
	/**
	 * 最高温度
	 */
    @DbField(name = "max_temp", type = DataType.TEXT)
	public String maxTemp;
	
	/**
	 * 最低温度
	 */
    @DbField(name = "min_temp", type = DataType.TEXT)
	public String minTemp;
	
	/**
	 * 天气类型（某些天气源每天有2种天气类型）
	 */
    @DbField(name = "weathers", type = DataType.INTARRAY)
	public int[] weather;
	
	/**
	 * 风力
	 */
    @DbField(name = "wind", type = DataType.TEXT)
	public String wind;

	/**
	 * 获取天气名称
	 * @Title: getWeatherName
	 * @param context
	 * @return
	 * @return String[]
	 * @date 2014-3-28 下午5:42:08
	 */
	public String[] getWeatherName(){
		return WeatherUtils.getWeather(weather);
	}
	
	/**
	 * 格式化天气名称。
	 * 如：晴转多云
	 * @Title: getFormatWeatherName
	 * @param context
	 * @return
	 * @return String
	 * @date 2014-3-29 下午3:03:01
	 */
	public String getFormatWeatherName(){
	    String[] names = getWeatherName();
	    StringBuilder sb = new StringBuilder(names[0]);
	    if(names.length > 1){
	        for(int i = 1; i < names.length; i++){
	            sb.append("转");
	            sb.append(names[i]);
	        }
	    }
	    return sb.toString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Weather> CREATOR
							= new Parcelable.Creator<Weather>() {
		public Weather createFromParcel(Parcel in) {
			Weather weather = new Weather();
			if(in.readInt() == 1){
				weather.calendar = (Calendar) in.readSerializable();
			}
			
			if(in.readInt() == 1){
				weather.currentTemp = in.readString();
			}

			if(in.readInt() == 1){
				weather.maxTemp = in.readString();
			}
			
			if(in.readInt() == 1){
				weather.minTemp = in.readString();
			}

			if(in.readInt() == 1){
				 in.readIntArray(weather.weather);
			}

			if(in.readInt() == 1){
				weather.wind = in.readString();
			}
			
			return weather;
		}

		public Weather[] newArray(int size) {
			return new Weather[size];
		}
	};

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		//calendar
		if(calendar == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeSerializable(calendar);
		}
		//currentTemp;
		if(currentTemp == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeString(currentTemp);
		}
		
		//maxTemp;
		if(maxTemp == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeString(maxTemp);
		}
		
		//minTemp;
		if(minTemp == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeString(minTemp);
		}
		
		//weather;
		if(weather == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeIntArray(weather);
		}
		
		//wind;
		if(wind == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeString(wind);
		}
	}
	
	
	public String toString(){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return "calendar:" + sdf.format(calendar.getTime()) + "; currentTemp:"
					+ currentTemp + "; " + "maxTemp:" + maxTemp + "; "
					+ "minTemp:" + minTemp + "; " + "weather:" + weather + "; "
					+ "wind:" + wind + ";";
		} catch (Exception e) {
			return "";
		}
	}

}
