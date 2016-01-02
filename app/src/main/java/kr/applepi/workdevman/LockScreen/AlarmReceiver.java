package kr.applepi.workdevman.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by qkswk on 2016-01-02.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch(intent.getAction()){
            case LockManager.ACTION_ALARM:
                LockManager.getInstance().sendAction(intent.getAction());
        }
    }
}
