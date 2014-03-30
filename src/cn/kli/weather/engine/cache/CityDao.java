package cn.kli.weather.engine.cache;

import android.content.Context;
import cn.kli.utils.dao.BaseDao;
import cn.kli.utils.dao.BaseDatabaseHelper;
import cn.kli.weather.engine.City;

class CityDao extends BaseDao<City> {

    public CityDao(Context context, BaseDatabaseHelper dbHelper) {
        super(context, City.class, dbHelper);
    }

}
