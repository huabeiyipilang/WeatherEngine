package cn.kli.weather.engine;

import java.util.ArrayList;
import java.util.List;

import weathersource.weather.com.cn.SourceWeatherComCn;
import weathersource.webxml.com.cn.SourceWebXml;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import cn.kli.weather.engine.cache.CacheManager;

public class WeatherEngine {
    //actions
    public final static String ACTION_FRESH_WIDGET_TIME = "cn.indroid.action.weather.freshwidgettime";
    public final static String ACTION_FRESH_WIDGET = "cn.indroid.action.weather.freshwidget";
    
    //shared prefs
    private final static String KEY_ENGINE_SOURCE = "engine_source";
	
	private final static String WEATHER_COM_CN = "��������̨";
	private final static String WEB_XML = "WebXml";
	
	private final static String[] SOURCE_LIST = {WEB_XML, WEATHER_COM_CN};
	
	private static WeatherEngine mInstance;
	private Context mContext;
	private WeatherSource mSource;
    private CacheManager mCache;
    
    private List<DataChangedListener> mListeners = new ArrayList<DataChangedListener>();
    
    private boolean isRequesting = false;
	
	private WeatherEngine(Context context){
		mContext = context;
        mCache = new CacheManager(mContext);
	}
	
	public static WeatherEngine getInstance(Context context){
		if(mInstance == null){
			mInstance = new WeatherEngine(context);
		}
		return mInstance;
	}
	
	public String[] getSourceList(){
		return SOURCE_LIST;
	}
	
	private WeatherSource getSourceByName(String name){
		WeatherSource res;
		if(WEATHER_COM_CN.equals(name)){
			res = new SourceWeatherComCn(mContext);
		}else if(WEB_XML.equals(name)){
			res = new SourceWebXml(mContext);
		}else{
			//default
			res = new SourceWebXml(mContext);
		}
		return res;
	}

    public interface DataChangedListener{
        void onWeatherChanged(City city);
        void onStateChanged(boolean isRequesting);
    }
    
    private void notifyWeatherChanged(City city) {
        synchronized (mListeners) {
            for (DataChangedListener listener : mListeners) {
                listener.onWeatherChanged(city);
            }
        }
    }
    
    private void notifyStateChanged(boolean isRequesting){
        synchronized(mListeners){
            for(DataChangedListener listener : mListeners){
                listener.onStateChanged(isRequesting);
            }
        }
    }
    
    public void register(DataChangedListener listener){
        synchronized(mListeners){
            mListeners.add(listener);
            listener.onStateChanged(isRequesting);
        }
    }
    
    public void unRegister(DataChangedListener listener){
        synchronized(mListeners){
            mListeners.remove(listener);
        }
    }
    /**
     * 
     * Get city datas from internet and write to database.
     */
    public void init(final Message callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                callback.arg1 = init();
                callback.sendToTarget();
            }
        }.start();
    }
    
    private int init(){
        mSource = getSourceByName(getEngineSource());
        int res = mSource.onInit();
        return res;
    }

    /**
     * Must be called in thread.
     * Get city weather from internet.
     */
    public City getWeatherByIndex(String index){
        return mSource.getWeatherByIndex(index);
    }
    
    public List<City> getCityList(City city){
        return mSource.getCityList(city);
    }
    
    public void changeEngineSource(final String source, final Message callback){
        AsyncTask.execute(new Runnable(){

            @Override
            public void run() {
                setEngineSource(source);
                callback.arg1 = init();
                callback.sendToTarget();
            }
            
        });
    }
    
    public boolean isRequesting(){
        return isRequesting;
    }
    
    /**
     * Must be called in thread.
     * Get city weather from internet.
     */
    synchronized public void requestWeatherByIndex(final String index){
        isRequesting = true;
        notifyStateChanged(isRequesting);
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                City city = getWeatherByIndex(index);

                if(city != null && city.weather != null){
                    //cache weather
                    mCache.cacheWeather(city);
                }
                isRequesting = false;
                notifyWeatherChanged(city);
                notifyStateChanged(isRequesting);
                Looper.loop();
            }
        }.start();
    }

    public void setDefaultMarkCity(City city){
        mCache.markDefaultCity(city, getEngineSource());
    }
    
    public boolean hasDefaultCity(){
        return mCache.hasDefaultCity();
    }
    
    public City getDefaultMarkCity(){
        
        try {
            return mCache.getDefaultCity();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
        
    }
    
    public void refreshWeather(){
        City city = getDefaultMarkCity();
        if(city != null){
            getWeatherByIndex(city.index);
        }
    }
    
    private void setEngineSource(String source){
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ENGINE_SOURCE, source);
        editor.commit();
    }
    
    private String getEngineSource(){
        SharedPreferences prefs = getPrefs();
        return prefs.getString(KEY_ENGINE_SOURCE, null);
    }
    
    private SharedPreferences getPrefs(){
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}
