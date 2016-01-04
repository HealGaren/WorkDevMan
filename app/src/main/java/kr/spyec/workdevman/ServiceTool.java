package kr.spyec.workdevman;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.List;

import kr.spyec.workdevman.Define.CDLockType;
import kr.spyec.workdevman.LockScreen.AlarmSingleTon;
import kr.spyec.workdevman.LockScreen.LockService;

/**
 * Created by qkswk on 2015-12-30.
 */
public class ServiceTool {

    private static boolean isRunning = false;

    public static boolean isServiceRunning(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals("kr.spyec.workdevman.LockScreen.LockService")) {
                return true;
            }
        }
        return false;
    }

    public static void reloadService(Context context) {
        SharedPreferences pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        int lockType = pref.getInt("lockType", CDLockType.NO_LOCK);
        switch (lockType) {
            case CDLockType.REPO_LOCK:
            case CDLockType.USER_LOCK:
                if (!isServiceRunning(context)) startService(context);
                break;
            case CDLockType.NO_LOCK:
                if (isServiceRunning(context)) stopService(context);
                break;
        }
    }


    public static void startService(Context context) {
        if (!isRunning) {
            isRunning = true;
            AlarmSingleTon.getInstance().init(context);
            AlarmSingleTon.getInstance().reloadAlarm();
            Intent i = new Intent(context, LockService.class);
            context.startService(i);
        }
    }

    public static void stopService(Context context) {
        if (isRunning) {
            isRunning = false;
            AlarmSingleTon.getInstance().cancelAlarm();
            Intent i = new Intent(context, LockService.class);
            context.stopService(i);
        }
    }
}
