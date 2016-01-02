package kr.applepi.workdevman.LockScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.media.MediaMetadataCompat;
import android.widget.Toast;

/**
 * Created by qkswk on 2015-12-31.
 */
public class ConnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // 네트웍에 변경이 일어났을때 발생하는 부분
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetInfo != null) {
                switch (activeNetInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE:
                    case ConnectivityManager.TYPE_WIFI:
                        LockManager.getInstance().sendAction(LockManager.ACTION_NETWORK_ON);
                }
            }

        }
    }
}
