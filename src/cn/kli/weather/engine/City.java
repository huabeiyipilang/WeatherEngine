package cn.kli.weather.engine;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import cn.kli.utils.dao.BaseInfo;
import cn.kli.utils.dao.DbField;
import cn.kli.utils.dao.DbField.DataType;

/**
 * 城市类
 * @Package cn.kli.weather.engine
 * @ClassName: City
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-3-28 下午5:30:53
 */
public class City extends BaseInfo implements Parcelable{
	/**
	 * 城市名称
	 */
    @DbField(name = "city_name", type = DataType.TEXT, isNull = false)
	public String name;
	
	/**
	 * 城市id（根据天气源不同而不同）
	 */
    @DbField(name = "city_index", type = DataType.TEXT, isNull = false)
	public String index;
	
	/**
	 * 未来N天的天气
	 */
	public ArrayList<Weather> weathers;

	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
		@SuppressWarnings("unchecked")
        public City createFromParcel(Parcel in) {
			City city = new City();
			if (in.readInt() == 1) {
				city.name = in.readString();
			}
			
			if(in.readInt() == 1){
				city.index = in.readString();
			}
			
			if(in.readInt() == 1){
				city.weathers = (ArrayList<Weather>) in.readSerializable();
			}

			return city;
		}

		public City[] newArray(int size) {
			return new City[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {

		//name
		if(name == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeString(name);
		}
		
		//id
		if(index == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeString(index);
		}
		
		//weathers
		if(weathers == null){
			parcel.writeInt(0);
		}else{
			parcel.writeInt(1);
			parcel.writeSerializable(weathers);
		}
	}
	

	@Override
	public String toString() {
		return "name:"+name+
				", id:"+id+
				"; ";
	}
}
