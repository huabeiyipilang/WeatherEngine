package cn.kli.weather.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Message;
import android.preference.PreferenceManager;
import cn.kli.weather.engine.cache.CacheManager;

/**
 * 天气引擎，提供查询天气、获取城市等功能。
 * @Package cn.kli.weather.engine
 * @ClassName: WeatherEngine
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-3-28 下午5:04:25
 */
public class WeatherEngine {
    private final static String KEY_ENGINE_SOURCE = "engine_source";

    private static WeatherEngine mInstance;
    private Context mContext;
    private WeatherSource mSource;
    private CacheManager mCache;

    private List<EngineListener> mListeners = new ArrayList<EngineListener>();

    private boolean isRequesting = false;

    private WeatherEngine(Context context) {
        mContext = context;
        mCache = new CacheManager(mContext);
    }

    /**
     * 获取天气引擎实例
     * @Title: getInstance
     * @param context
     * @return
     * @return WeatherEngine
     * @date 2014-3-28 下午5:05:45
     */
    public static WeatherEngine getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WeatherEngine(context);
        }
        return mInstance;
    }

    private void notifyWeatherChanged(City city) {
        synchronized (mListeners) {
            for (EngineListener listener : mListeners) {
                listener.onWeatherChanged(city);
            }
        }
    }

    private void notifyStateChanged(boolean isRequesting) {
        synchronized (mListeners) {
            for (EngineListener listener : mListeners) {
                listener.onRequestStateChanged(isRequesting);
            }
        }
    }

    /**
     * 注册回掉。
     * 注册后立即回调请求状态。
     * @Title: register
     * @param listener
     * @return void
     * @date 2014-3-28 下午5:12:02
     */
    public void addListener(EngineListener listener) {
        synchronized (mListeners) {
            mListeners.add(listener);
            listener.onRequestStateChanged(isRequesting);
        }
    }

    /**
     * 注销回调
     * @Title: unListen
     * @param listener
     * @return void
     * @date 2014-3-28 下午5:14:37
     */
    public void removeListener(EngineListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    
    /**
     * 初始化天气引擎
     * @Title: init
     * @param source
     * @param callback
     * @return void
     * @date 2014-3-28 下午5:17:52
     */
    public void init(WeatherSource source) {
        mSource = source;
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                int res = mSource.onInit();
                if(res == ErrorCode.SUCCESS){
                    for(EngineListener listener : mListeners){
                        listener.onInited();
                    }
                }else{
                    for(EngineListener listener : mListeners){
                        listener.onError(res);
                    }
                }
            }

        });
    }

    /**
     * 获取子城市列表
     * @Title: requestCityListByCity
     * @param city
     * @param callback
     * @return void
     * @date 2014-3-29 下午4:56:02
     */
    public void requestCityListByCity(final City city, final Message callback) {
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                List<City> list = mSource.getCityList(city);
                if (callback != null) {
                    callback.obj = list;
                    callback.sendToTarget();
                }
            }

        });
    }

    /**
     * 是否正在请求
     * @Title: isRequesting
     * @return
     * @return boolean
     * @date 2014-3-29 下午9:21:53
     */
    public boolean isRequesting() {
        return isRequesting;
    }


    /**
     * 通过天气源，根据城市id请求天气数据
     * @Title: requestWeatherByCityId
     * @param id
     * @return void
     * @date 2014-3-29 下午9:22:06
     */
    synchronized public void requestWeatherByCityIndex(final String index) {
        isRequesting = true;
        notifyStateChanged(isRequesting);
        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {
                City city = mSource.getWeatherByCityIndex(index);

                if (city != null && city.weathers != null) {
                    // cache weather
                    mCache.cacheWeather(city);
                }
                isRequesting = false;
                notifyWeatherChanged(city);
                notifyStateChanged(isRequesting);
            }

        });
    }

    /**
     * 收藏城市
     * @Title: markCity
     * @param city
     * @return void
     * @date 2014-3-29 下午9:23:18
     */
    public void markCity(City city) {
        mCache.markCity(city);
    }

    /**
     * 获取收藏城市列表
     * @Title: getMarkCity
     * @return
     * @return List<City>
     * @date 2014-3-29 下午9:23:41
     */
    public List<City> getMarkCity() {
        return mCache.getMarkedCities();
    }
    
    /**
     * 删除收藏城市
     * @Title: removeCity
     * @param city
     * @return void
     * @date 2014-3-30 上午9:46:51
     */
    public void removeCity(City city){
        mCache.removeCity(city);
    }

    private void setEngineSource(String source) {
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ENGINE_SOURCE, source);
        editor.commit();
    }

    private String getEngineSource() {
        SharedPreferences prefs = getPrefs();
        return prefs.getString(KEY_ENGINE_SOURCE, null);
    }

    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

}
