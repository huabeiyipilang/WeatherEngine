package cn.kli.weather.engine.location;

abstract class BaseLocation {
    abstract String[] requestLocByCoordinate(float longitude, float latitude);
}
