package cn.kli.weather.engine;

/**
 * 天气引擎回调
 * @Package cn.kli.weather.engine
 * @ClassName: DataChangedListener
 * @author Carl Li
 * @mail huabeiyipilang@gmail.com
 * @date 2014-3-28 下午5:06:15
 */
public class EngineListener {
    
    
    /**
     * 天气数据变化
     * @Title: onWeatherChanged
     * @param city 内有最新天气信息
     * @return void
     * @date 2014-3-28 下午5:18:31
     */
    protected void onWeatherChanged(City city){
        
    }
    
    
    /**
     * 回调请求状态
     * @Title: onRequestStateChanged
     * @param isRequesting
     * @return void
     * @date 2014-3-28 下午5:16:07
     */
    protected void onRequestStateChanged(boolean isRequesting){
        
    }
    
    
    /**
     * 初始化成功。
     * 初始化失败会在onError中回调
     * @Title: onInited
     * @return void
     * @date 2014-3-28 下午5:22:50
     */
    protected void onInited(){
        
    }
    
    
    /**
     * 错误回调，详见{@link ErrorCode}
     * @Title: onError
     * @param errorCode
     * @return void
     * @date 2014-3-28 下午5:23:22
     */
    protected void onError(int errorCode){
        
    }
    
    /**
     * 请求城市列表回调
     * @Title: onCityListReceived
     * @param city
     * @return void
     * @date 2014-3-29 下午5:04:40
     */
    protected void onCityListReceived(City city){
        
    }
}
