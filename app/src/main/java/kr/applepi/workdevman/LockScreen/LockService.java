package kr.applepi.workdevman.LockScreen;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

/**
 * Created by qkswk on 2015-11-24.
 */
public class LockService extends Service {

    private ScreenReceiver sReceiver = null;
    private PackageReceiver pReceiver = null;
    private ConnReceiver cReceiver = null;
    private AlarmReceiver aReceiver = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        startForeground(1, new Notification());

        sReceiver = new ScreenReceiver();
        IntentFilter sFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(sReceiver, sFilter);

        pReceiver = new PackageReceiver();
        IntentFilter pFilter = new IntentFilter();
        pFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        pFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        pFilter.addDataScheme("package");
        registerReceiver(pReceiver, pFilter);

        cReceiver = new ConnReceiver();
        IntentFilter cFilter = new IntentFilter();
        cFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(cReceiver, cFilter);

        aReceiver = new AlarmReceiver();
        IntentFilter aFilter = new IntentFilter();
        registerReceiver(aReceiver, aFilter);

        LockManager.getInstance().init(this);

        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sReceiver != null) {
            unregisterReceiver(sReceiver);
        }
        if (pReceiver != null) {
            unregisterReceiver(pReceiver);
        }
        if (cReceiver != null){
            unregisterReceiver(cReceiver);
        }
    }

}