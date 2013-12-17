package cn.kli.weather.engine;

interface RequestCommands {
    public final static int CMD_INIT = 1;
    public final static int CMD_REQUEST_CITY_LIST_BY_CITY = 2;
    public final static int CMD_REQUEST_WEATHER_BY_INDEX = 3;
}
