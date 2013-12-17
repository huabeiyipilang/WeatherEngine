package cn.kli.weather.engine;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UpdateAlarmManager {
    public final static String ACTION_UPDATE_WEATHER = "cn.indroid.action.updateweather";
    public final static String ACTION_NOTIFY_WEATHER = "cn.indroid.action.notifyweather";
    
    private final static String PREF_UPDATE_TIME = "update_time";
    private final static String PREF_UPDATE_DURING = "update_during";

    private static UpdateAlarmManager sInstance;
    
    private Context mContext;
    
    private UpdateAlarmManager(Context context){
        mContext = context;
    }
    
    public static UpdateAlarmManager getInstance(Context context){
        if(sInstance == null){
            sInstance = new UpdateAlarmManager(context);
        }
        return sInstance;
    }

    public void setUpdateDuring(int hour){
        hour = hour * 60 * 60 * 1000;
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_UPDATE_DURING, hour);
        editor.commit();
        freshAlarmTime();
    }
    
    public int getUpdateDuring(){
        SharedPreferences prefs = getPrefs();
        return prefs.getInt(PREF_UPDATE_DURING, 4 * 60 * 60 * 1000);
    }

    public void setUpdateTime(int time){
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_UPDATE_TIME, time);
        editor.commit();
        initNotifyAlarm();
    }
    
    public int getUpdateTime(){
        SharedPreferences prefs = getPrefs();
        return prefs.getInt(PREF_UPDATE_TIME, 0);
    }
    
    
    private SharedPreferences getPrefs(){
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    
    private void freshAlarmTime() {
        AlarmManager am = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_UPDATE_WEATHER);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = getUpdateDuring();
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, sender);
    }
    
    private void initNotifyAlarm(){
        AlarmManager am = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ACTION_NOTIFY_WEATHER);
        PendingIntent sender = PendingIntent.getBroadcast(mContext, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        Calendar notifyToday = Calendar.getInstance();
        notifyToday.set(Calendar.HOUR_OF_DAY, getUpdateTime());
        notifyToday.set(Calendar.MINUTE, 0);
        notifyToday.set(Calendar.SECOND, 0);
        notifyToday.set(Calendar.MILLISECOND, 0);
        if(cal.after(notifyToday)){
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        int interval = 1000*60*60*24;
        am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, sender);
    }
}
