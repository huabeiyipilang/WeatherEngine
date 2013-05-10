package cn.kli.weather.engine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author kli
 *
 */

public class Weather implements Parcelable{
	//√ª”– ˝æ›
	public final static int W_NO_DATA = 0;
	//«ÁÃÏ
	public final static int W_QING = 1;
	//∂‡‘∆
	public final static int W_DUOYUN = 2;
	//“ı
	public final static int W_YIN = 3;
	//’Û”Í
	public final static int W_ZHENYU = 4;
	//¿◊’Û”Í
	public final static int W_LEIZHENYU = 5;
	//¿◊’Û”Í≤¢∞È”–±˘±¢
	public final static int W_LEIZHENYUBINGBANYOUBINGBAO = 6;
	//”Íº–—©
	public final static int W_YUJIAXUE = 7;
	//–°”Í
	public final static int W_XIAOYU = 8;
	//÷–”Í
	public final static int W_ZHONGYU = 9;
	//¥Û”Í
	public final static int W_DAYU = 10;
	//±©”Í
	public final static int W_BAOYU = 11;
	//¥Û±©”Í
	public final static int W_DABAOYU = 12;
	//Ãÿ¥Û±©”Í
	public final static int W_TEDABAOYU = 13;
	//’Û—©
	public final static int W_ZHENXUE = 14;
	//–°—©
	public final static int W_XIAOXUE = 15;
	//÷–—©
	public final static int W_ZHONGXUE = 16;
	//¥Û—©
	public final static int W_DAXUE = 17;
	//±©—©
	public final static int W_BAOXUE = 18;
	//ŒÌ
	public final static int W_WU = 19;
	//∂≥”Í
	public final static int W_DONGYU = 20;
	//…≥≥æ±©
	public final static int W_SHACHENBAO = 21;
	//–°”Í-÷–”Í
	public final static int W_XIAOYUZHONGYU = 22;
	//÷–”Í-¥Û”Í
	public final static int W_ZHONGYUDAYU = 23;
	//¥Û”Í-±©”Í
	public final static int W_DAYUBAOYU = 24;
	//±©”Í-¥Û±©”Í
	public final static int W_BAOYUDABAOYU = 25;
	//¥Û±©”Í-Ãÿ¥Û±©”Í
	public final static int W_DABAOYUTEDABAOYU = 26;
	//–°—©-÷–—©
	public final static int W_XIAOXUEZHONGXUE = 27;
	//÷–—©-¥Û—©
	public final static int W_ZHONGXUEDAXUE = 28;
	//¥Û—©-±©—©
	public final static int W_DAXUEBAOXUE = 29;
	//∏°≥æ
	public final static int W_FUCHEN = 30;
	//—Ô…≥
	public final static int W_YANGSHA = 31;
	//«ø…≥≥æ±©
	public final static int W_QIANGSHACHENBAO = 32;
	
	public Calendar calendar;
	public String currentTemp;
	public String maxTemp;
	public String minTemp;
	public int[] weather;
	public String wind;

	public String getWeatherName(Context context){
		return WeatherUtils.getWeather(context, weather);
	}
	
	public int getIcon(){
		return WeatherUtils.getDrawable(weather);
	}
	
	public boolean isBadWeather(){
		return WeatherUtils.isBadWeather(this);
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍMM‘¬dd»’");
			return "calendar:" + sdf.format(calendar.getTime()) + "; currentTemp:"
					+ currentTemp + "; " + "maxTemp:" + maxTemp + "; "
					+ "minTemp:" + minTemp + "; " + "weather:" + weather + "; "
					+ "wind:" + wind + ";";
		} catch (Exception e) {
			return "";
		}
	}

}
