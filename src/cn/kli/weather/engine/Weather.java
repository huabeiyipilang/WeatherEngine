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
	//û�����
	public final static int W_NO_DATA = 0;
	//����
	public final static int W_QING = 1;
	//����
	public final static int W_DUOYUN = 2;
	//��
	public final static int W_YIN = 3;
	//����
	public final static int W_ZHENYU = 4;
	//������
	public final static int W_LEIZHENYU = 5;
	//�����겢���б�
	public final static int W_LEIZHENYUBINGBANYOUBINGBAO = 6;
	//���ѩ
	public final static int W_YUJIAXUE = 7;
	//С��
	public final static int W_XIAOYU = 8;
	//����
	public final static int W_ZHONGYU = 9;
	//����
	public final static int W_DAYU = 10;
	//����
	public final static int W_BAOYU = 11;
	//����
	public final static int W_DABAOYU = 12;
	//�ش���
	public final static int W_TEDABAOYU = 13;
	//��ѩ
	public final static int W_ZHENXUE = 14;
	//Сѩ
	public final static int W_XIAOXUE = 15;
	//��ѩ
	public final static int W_ZHONGXUE = 16;
	//��ѩ
	public final static int W_DAXUE = 17;
	//��ѩ
	public final static int W_BAOXUE = 18;
	//��
	public final static int W_WU = 19;
	//����
	public final static int W_DONGYU = 20;
	//ɳ����
	public final static int W_SHACHENBAO = 21;
	//С��-����
	public final static int W_XIAOYUZHONGYU = 22;
	//����-����
	public final static int W_ZHONGYUDAYU = 23;
	//����-����
	public final static int W_DAYUBAOYU = 24;
	//����-����
	public final static int W_BAOYUDABAOYU = 25;
	//����-�ش���
	public final static int W_DABAOYUTEDABAOYU = 26;
	//Сѩ-��ѩ
	public final static int W_XIAOXUEZHONGXUE = 27;
	//��ѩ-��ѩ
	public final static int W_ZHONGXUEDAXUE = 28;
	//��ѩ-��ѩ
	public final static int W_DAXUEBAOXUE = 29;
	//����
	public final static int W_FUCHEN = 30;
	//��ɳ
	public final static int W_YANGSHA = 31;
	//ǿɳ����
	public final static int W_QIANGSHACHENBAO = 32;
	//霾
	public final static int W_MAI = 33;
	
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
