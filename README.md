WeatherEngine
=============

###使用简介
####1、初始化天气引擎

    //设置监听
    WeatherEngine.getInstance(this).addListener(new EngineListener(){
        @Override
        //监听初始化回调
        protected void onInitFinished(int res) {
            if(res == ErrorCode.SUCCESS){
                //TODO:初始化成功
            }else{
                //TODO:初始化失败
            }
        }
    });
    
    //使用天气源初始化天气引擎
    WeatherEngine.getInstance(this).init(new SourceWebXml(this));


----------


####2、请求与回调

请求：下列请求均为异步请求，返回值为请求编号。

    //请求城市列表，参数为null时，表示请求根城市列表
    public int requestCityListByCity(City city)
    
    //请求天气
    public int requestWeatherByCity(City city)



回调：
    
    EngineListener mListener = new EngineListener(){
    
        @Override
        //请求状态回调
        protected void onRequestStateChanged(boolean isRequesting) {}
        
        @Override
        //初始化回调
        protected void onInitFinished(int res) {}
        
        @Override
        //城市列表请求回调
        protected void onCityListResponse(int res, int requestId, List<City> list) {}
        
        @Override
        //天气请求回调
        protected void onWeatherResponse(int res, int requestId, City city) {}
        
    };
    
    //设置回调
    WeatherEngine.getInstance(this).addListener(mListener);
    
    //注销回调
    WeatherEngine.getInstance(this).removeListener(mListener);
    


----------


####3、其它接口：

    //获取请求状态
    public boolean isRequesting()
    
    //收藏城市
    public void markCity(City city)
    
    //删除收藏城市
    public void removeCity(City city)
    
    //获取收藏城市列表
    public java.util.List<City> getMarkCity()
    
    


----------


###天气源：

####1、WebXml

网址：http://www.webxml.com.cn/zh_cn/index.aspx

项目地址：  https://github.com/huabeiyipilang/SourceWebXml

####2、中国天气网

网址：http://www.weather.com.cn/

项目地址：https://github.com/huabeiyipilang/SourceWeatherComCn


----------

###DEMO：
https://github.com/huabeiyipilang/WeatherApp