package kr.spyec.workdevman.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kr.spyec.workdevman.ServiceTool;

/**
 * Created by qkswk on 2015-11-24.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ServiceTool.startService(context);
        }
    }
}