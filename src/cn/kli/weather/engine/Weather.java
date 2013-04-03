package cn.kli.weather.engine;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author kli
 *
 *
 *        0          ��          
          1          ����          
          2          ��          
          3          ����          
          4          ������          
          5          �����겢���б���          
          6          ���ѩ          
          7          С��          
          8          ����          
          9          ����          
          10         ����          
          11         ����          
          12         �ش���          
          13         ��ѩ          
          14         Сѩ          
          15         ��ѩ          
          16         ��ѩ          
          17         ��ѩ          
          18         ��          
          19         ����          
          20         ɳ����          
          21         С��-����          
          22         ����-����          
          23         ����-����          
          24         ����-����          
          25         ����-�ش���          
          26         Сѩ-��ѩ          
          27         ��ѩ-��ѩ          
          28         ��ѩ-��ѩ          
          29         ����          
          30         ��ɳ          
          31         ǿɳ����          
          nothing    û������
 */

public class Weather implements Parcelable{
	public Calendar calendar;
	public String currentTemp;
	public String maxTemp;
	public String minTemp;
	public String weather;
	public String wind;

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
				weather.weather = in.readString();
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
			parcel.writeString(weather);
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
			return "calendar:" + sdf.format(calendar.getTime()) + "; currentTemp:"
					+ currentTemp + "; " + "maxTemp:" + maxTemp + "; "
					+ "minTemp:" + minTemp + "; " + "weather:" + weather + "; "
					+ "wind:" + wind + ";";
		} catch (Exception e) {
			return "";
		}
	}

}
