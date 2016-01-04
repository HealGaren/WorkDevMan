package kr.spyec.workdevman.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kr.spyec.workdevman.ServiceTool;

/**
 * Created by qkswk on 2015-11-24.
 */
public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action) {
            case Intent.ACTION_PACKAGE_ADDED:
            case Intent.ACTION_PACKAGE_REMOVED:
            case Intent.ACTION_PACKAGE_REPLACED:
                ServiceTool.startService(context);
                break;
        }
    }
}