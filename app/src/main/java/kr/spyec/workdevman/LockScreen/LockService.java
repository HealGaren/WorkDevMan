package kr.spyec.workdevman.LockScreen;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import kr.spyec.workdevman.Activity.MainActivity;
import kr.spyec.workdevman.Activity.SplashActivity;
import kr.spyec.workdevman.R;

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

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.icon_splash)
                .setTicker("잠금 서비스가 실행되었습니다.")
                .setContentTitle("일해라! 개발자의 화면 잠금이 실행중입니다.")
                .setContentText("터치해서 설정합니다.")
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1, notification);

        sReceiver = new ScreenReceiver();
        IntentFilter sFilter = new IntentFilter();
        sFilter.addAction(Intent.ACTION_SCREEN_ON);
        sFilter.addAction(Intent.ACTION_SCREEN_OFF);
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
        if (cReceiver != null) {
            unregisterReceiver(cReceiver);
        }
    }

}