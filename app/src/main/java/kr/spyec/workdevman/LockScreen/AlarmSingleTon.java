package kr.spyec.workdevman.LockScreen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import kr.spyec.workdevman.Define.CDLockType;

/**
 * Created by qkswk on 2015-12-31.
 */
public class AlarmSingleTon {
    private static AlarmSingleTon instance = null;

    public static AlarmSingleTon getInstance() {
        if (instance == null) {
            instance = new AlarmSingleTon();
        }
        return instance;
    }

    private boolean isInited = false;


    private AlarmManager alarmManager;
    private PendingIntent alarmSender;
    private boolean m_isRunning;
    private SharedPreferences pref;

    public void init(Context context){
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction(LockManager.ACTION_ALARM);
        alarmSender = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        m_isRunning = false;
        pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        isInited = true;
    }

    public void cancelAlarm() {
        alarmManager.cancel(alarmSender);
        m_isRunning = false;
    }

    public void reloadAlarm() {
        long nextDateMill = pref.getLong("nextDate", 0);


        int lockType = pref.getInt("lockType", CDLockType.NO_LOCK);

        if (lockType != CDLockType.NO_LOCK) {

            alarmManager.set(AlarmManager.RTC, nextDateMill, alarmSender);
            m_isRunning = true;

        } else {
            m_isRunning = false;
        }
    }

    public void setNewAlarm() {
        int delayDay = pref.getInt("day", 1);
        int hour = pref.getInt("hour", 7);
        int minute = pref.getInt("minute", 0);

        Calendar cal = Calendar.getInstance();
        long startDateMill = cal.getTimeInMillis();

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.add(Calendar.DATE, delayDay);

        long nextDateMill = cal.getTimeInMillis();
        pref.edit()
                .putLong("nextDate", nextDateMill)
                .putLong("startDate", startDateMill)
                .apply();
        alarmManager.set(AlarmManager.RTC, nextDateMill, alarmSender);

    }


    public void setNextAlarm() {
        int delayDay = pref.getInt("day", 1);
        long startDateMill = pref.getLong("nextDate", 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDateMill);
        cal.add(Calendar.DATE, delayDay);

        long nextDateMill = cal.getTimeInMillis();
        pref.edit()
                .putLong("nextDate", nextDateMill)
                .putLong("startDate", startDateMill)
                .apply();
        alarmManager.set(AlarmManager.RTC, nextDateMill, alarmSender);

    }

    public boolean isRunning() {
        return m_isRunning;
    }

    public boolean isInited() {
        return isInited;
    }
}
