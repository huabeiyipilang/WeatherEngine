package cn.kli.weather.engine.cache;

import android.content.Context;
import cn.kli.utils.dao.BaseDatabaseHelper;
import cn.kli.weather.engine.City;
import cn.kli.weather.engine.Weather;

class WeatherDbHelper extends BaseDatabaseHelper {
    private final static String DB_NAME = "weather.db";
    private final static int DB_VERSION = 1;
    
    public WeatherDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        registerTable(City.class);
        registerTable(Weather.class);
    }

}
