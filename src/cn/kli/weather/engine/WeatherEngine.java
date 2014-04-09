package cn.kli.weather.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.util.SparseArrayCompat;
import cn.kli.weather.engine.cache.CacheManager;

/**
 * 天气引擎，提供查询天气、获取城市等功能。
 * 
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
    private RequestManager mRequestManager;

    private List<EngineListener> mListeners = new ArrayList<EngineListener>();

    private WeatherEngine(Context context) {
        mContext = context;
        mCache = new CacheManager(mContext);
        mRequestManager = new RequestManager();
    }

    /**
     * 获取天气引擎实例
     * 
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

    /**
     * 注册回掉。 注册后立即回调请求状态。
     * 
     * @Title: register
     * @param listener
     * @return void
     * @date 2014-3-28 下午5:12:02
     */
    public void addListener(EngineListener listener) {
        synchronized (mListeners) {
            mListeners.add(listener);
            listener.onRequestStateChanged(mRequestManager.isRequesting);
        }
    }

    /**
     * 注销回调
     * 
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
     * 
     * @Title: init
     * @param source
     * @param callback
     * @return void
     * @date 2014-3-28 下午5:17:52
     */
    public int init(WeatherSource source) {
        mSource = source;
        return mRequestManager.request(new Request() {

            @Override
            public void run() {
                int res = mSource.onInit();
                synchronized (mListeners) {
                    for (EngineListener listener : mListeners) {
                        listener.onInitFinished(res);
                    }
                }
            }

        });
    }

    /**
     * 获取子城市列表
     * 
     * @Title: requestCityListByCity
     * @param city
     * @param callback
     * @return void
     * @date 2014-3-29 下午4:56:02
     */
    public int requestCityListByCity(final City city) {
        return mRequestManager.request(new Request() {

            @Override
            public void run() {
                List<City> list = new ArrayList<City>();
                int res = mSource.requestCityList(city, list);

                synchronized (mListeners) {
                    for (EngineListener listener : mListeners) {
                        listener.onCityListResponse(res, index, list);
                    }
                }
            }

        });
    }

    /**
     * 是否正在请求
     * 
     * @Title: isRequesting
     * @return
     * @return boolean
     * @date 2014-3-29 下午9:21:53
     */
    public boolean isRequesting() {
        return mRequestManager.isRequesting;
    }

    /**
     * 通过天气源，根据城市id请求天气数据
     * 
     * @Title: requestWeatherByCityId
     * @param id
     * @return void
     * @date 2014-3-29 下午9:22:06
     */
    synchronized public int requestWeatherByCity(final City city) {
        return mRequestManager.request(new Request() {

            @Override
            public void run() {
                int res = mSource.requestWeatherByCity(city);

                if (res == ErrorCode.SUCCESS && city != null && city.weathers != null) {
                    // cache weather
                    mCache.cacheWeather(city);
                }

                synchronized (mListeners) {
                    for (EngineListener listener : mListeners) {
                        listener.onWeatherResponse(res, index, city);
                    }
                }
            }

        });
    }

    /**
     * 收藏城市
     * 
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
     * 
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
     * 
     * @Title: removeCity
     * @param city
     * @return void
     * @date 2014-3-30 上午9:46:51
     */
    public void removeCity(City city) {
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

    private class Request implements Runnable {
        int index;

        @Override
        public void run() {
        }
    }

    private class RequestManager {
        private int requestIndex = 1000;
        private SparseArrayCompat<Request> requestList = new SparseArrayCompat<Request>();
        private boolean isRequesting = false;

        public synchronized int request(Request request) {
            int index = requestIndex++;
            request.index = index;
            if (isRequesting) {
                requestList.put(index, request);
            } else {
                changeRequestState(true);
                startAsyncTask(request);
            }

            return index;
        }

        private void startAsyncTask(final Request request) {
            new AsyncTask<Object, Object, Object>() {

                @Override
                protected Object doInBackground(Object... params) {
                    int index = request.index;
                    request.run();
                    return index;
                }

                @Override
                protected void onPostExecute(Object result) {
                    super.onPostExecute(result);
                    int index = (Integer) result;
                    onRequestFinish(index);
                }

            }.execute("");
        }

        private void onRequestFinish(int index) {
            if (requestList.size() == 0) {
                changeRequestState(false);
                return;
            }

            Request request = null;

            do {
                request = requestList.get(index++);
            } while (request == null);

            requestList.remove(request.index);
            startAsyncTask(request);
        }

        private void changeRequestState(boolean request) {
            isRequesting = request;
            synchronized (mListeners) {
                for (EngineListener listener : mListeners) {
                    listener.onRequestStateChanged(isRequesting);
                }
            }
        }
    }
}
