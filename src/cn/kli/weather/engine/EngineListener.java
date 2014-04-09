package cn.kli.weather.engine;

import java.util.List;

/**
 * 天气引擎回调
 * 
 * @Package cn.kli.weather.engine
 * @ClassName: EngineListener
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-3-28 下午5:06:15
 */
public class EngineListener {

    /**
     * 天气数据变化
     * 
     * @Title: onWeatherChanged
     * @param city
     *            内有最新天气信息
     * @return void
     * @date 2014-3-28 下午5:18:31
     */
    private void onWeatherChanged(City city) {

    }

    /**
     * 回调请求状态
     * 
     * @Title: onRequestStateChanged
     * @param isRequesting
     * @return void
     * @date 2014-3-28 下午5:16:07
     */
    protected void onRequestStateChanged(boolean isRequesting) {

    }

    /**
     * 初始化回调
     * 
     * @Title: onInitFinished
     * @param res
     * @return void
     * @date 2014-4-4 下午3:42:43
     */
    protected void onInitFinished(int res) {

    }

    /**
     * 城市列表查询回调
     * 
     * @Title: onCityListResponse
     * @param res
     * @param requestId
     * @param list
     * @return void
     * @date 2014-4-4 下午3:43:00
     */
    protected void onCityListResponse(int res, int requestId, List<City> list) {

    }

    /**
     * 天气查询回调。
     * 
     * @Title: onWeatherResponse
     * @param res
     * @param requestId
     * @param city
     * @return void
     * @date 2014-4-4 下午3:45:01
     */
    protected void onWeatherResponse(int res, int requestId, City city) {

    }

}
