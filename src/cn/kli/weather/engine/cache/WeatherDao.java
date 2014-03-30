package cn.kli.weather.engine.cache;

import android.content.Context;
import cn.kli.utils.dao.BaseDao;
import cn.kli.utils.dao.BaseDatabaseHelper;
import cn.kli.weather.engine.Weather;

class WeatherDao extends BaseDao<Weather> {

    public WeatherDao(Context context, BaseDatabaseHelper dbHelper) {
        super(context, Weather.class, dbHelper);
    }

}
