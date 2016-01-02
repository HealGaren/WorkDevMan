package kr.applepi.workdevman.LockScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.AsyncTask;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import kr.applepi.workdevman.Define.CDLockType;
import kr.applepi.workdevman.HttpService.API.GitRepoData;
import kr.applepi.workdevman.HttpService.API.GitRepoDataService;
import kr.applepi.workdevman.HttpService.API.GitRepoListService;
import kr.applepi.workdevman.HttpService.NetServiceFactory;
import kr.applepi.workdevman.R;
import retrofit.Call;

/**
 * Created by qkswk on 2016-01-02.
 */
public class LockManager implements View.OnTouchListener {

    private static LockManager instance = null;

    public static LockManager getInstance() {
        if (instance == null) instance = new LockManager();
        return instance;
    }


    private boolean isTimeOver = false;

    private boolean isNetworkFailed = false;

    private boolean isPhoneIdleFailed = false;


    private boolean isPhoneIdle = true;


    private boolean isShowing = false;

    private WindowManager windowManager;
    private View view;

    private AnimationSet addAnim;

    private LinearLayout lockScreenLayout;

    private ImageView lockImage;

    private TextView extraTimeText;
    private TextView lockStrText;
    private TextView pushedTimeText;

    private AdView adView;
    private AdRequest.Builder builder;

    private float firstTouchY;
    private float sizeY;

    private String lockStr;
    private String lockStrWorked;
    private String lockStrNoWorked;
    private String lockStrNetwork;

    private SharedPreferences pref;

    private String accessToken;
    private String username;

    private boolean isUnlockable = true;

    public static final String ACTION_ALARM = "ActionAlarm";
    public static final String ACTION_NETWORK_ON = "ActionNetworkOn";


    private LockManager() {
    }

    public void sendAction(String action) {
        switch (action) {
            case ACTION_ALARM:
                isTimeOver = true;
                if (isPhoneIdle) new NetTask().execute();
                else isPhoneIdleFailed = true;
                break;

            case Intent.ACTION_SCREEN_ON:
                if (isPhoneIdle) new NetTask().execute();
                else isPhoneIdleFailed = true;
                break;
            case Intent.ACTION_SCREEN_OFF:
                removeView();
                break;

            case ACTION_NETWORK_ON:
                if (isNetworkFailed) {
                    if (isPhoneIdle) {
                        new NetTask().execute();
                        isNetworkFailed = false;
                    } else isPhoneIdleFailed = true;
                }
                break;
        }

    }

    public void lock() {
        if (isPhoneIdle) new NetTask().execute();
    }

    public void init(Context context) {
        pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        accessToken = pref.getString("accessToken", "");
        username = pref.getString("username", "");

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        sizeY = size.y;

        addAnim = new AnimationSet(true);
        addAnim.setInterpolator(new DecelerateInterpolator());
        addAnim.addAnimation(new TranslateAnimation(0, 0, -sizeY * 0.4f * 1.5f, 0));
        addAnim.addAnimation(new AlphaAnimation(0, 1));
        addAnim.setDuration(300);

        Resources res = context.getResources();
        lockStr = res.getString(R.string.lock_str);
        lockStrWorked = res.getString(R.string.lock_str_worked);
        lockStrNoWorked = res.getString(R.string.lock_str_no_worked);
        lockStrNetwork = res.getString(R.string.lock_str_network);

        view = LayoutInflater.from(context).inflate(R.layout.activity_lock_screen, null);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        lockScreenLayout = (LinearLayout) view.findViewById(R.id.layout_lock_screen);

        lockImage = (ImageView) view.findViewById(R.id.image_lock);
        extraTimeText = (TextView) view.findViewById(R.id.text_extra_time);
        lockStrText = (TextView) view.findViewById(R.id.text_lock_str);
        pushedTimeText = (TextView) view.findViewById(R.id.text_pushed_time);

        adView = (AdView) view.findViewById(R.id.adView);
        builder = new AdRequest.Builder();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                removeView();
                super.onAdOpened();
            }
        });

        view.findViewById(R.id.btn_unlock).setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstTouchY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = firstTouchY - event.getRawY();
                Log.wtf("", String.valueOf(deltaY));
                if (deltaY > 0) {
                    lockScreenLayout.setTranslationY(-deltaY * 1.5f);
                    if (deltaY >= sizeY * 0.4f) lockScreenLayout.setAlpha(0);
                    else lockScreenLayout.setAlpha(1f - deltaY / (sizeY * 0.4f));
                } else {
                    lockScreenLayout.setAlpha(1);
                    lockScreenLayout.setTranslationY(0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (firstTouchY - event.getRawY() >= sizeY * 0.15f && isUnlockable) {
                    removeView();
                } else {
                    backView();
                }
                break;
        }

        return true;
    }


    private void addView() {

        lockScreenLayout.setAlpha(1);
        lockScreenLayout.setTranslationY(0);

        adView.loadAd(builder.build());

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );

        if (!isShowing) {
            windowManager.addView(view, params);
            lockScreenLayout.startAnimation(addAnim);
            isShowing = true;
        }
    }

    private void backView() {
        if (isShowing) {
            AnimationSet anim = new AnimationSet(true);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.addAnimation(new TranslateAnimation(
                    0, 0, lockScreenLayout.getTranslationY(), 0));
            anim.addAnimation(new AlphaAnimation(lockScreenLayout.getAlpha(), 1));
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    lockScreenLayout.setTranslationY(0);
                    lockScreenLayout.setAlpha(1);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            lockScreenLayout.setTranslationY(0);
            lockScreenLayout.setAlpha(1);
            lockScreenLayout.startAnimation(anim);
        }
    }

    private void removeView() {
        if (isShowing) {
            AnimationSet anim = new AnimationSet(true);
            anim.setInterpolator(new AccelerateInterpolator());
            anim.addAnimation(new TranslateAnimation(
                    0, 0, lockScreenLayout.getTranslationY(), -sizeY * 0.4f * 1.5f));
            anim.addAnimation(new AlphaAnimation(lockScreenLayout.getAlpha(), 0));
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    windowManager.removeView(view);
                    isShowing = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            lockScreenLayout.setTranslationY(0);
            lockScreenLayout.setAlpha(1);
            lockScreenLayout.startAnimation(anim);
        }
    }


    private class NetTask extends AsyncTask<Void, Void, Long> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Long doInBackground(Void... params) {
            int lockType = pref.getInt("lockType", CDLockType.NO_LOCK);
            String repoName = pref.getString("repoName", "");
            try {

                if (lockType == CDLockType.REPO_LOCK) {
                    Call<GitRepoData> call = NetServiceFactory
                            .createAPIService(GitRepoDataService.class)
                            .loadRepoData(username, repoName, accessToken);
                    return call.execute().body().getPushDateLocalTime();
                } else {
                    Call<List<GitRepoData>> call = NetServiceFactory
                            .createAPIService(GitRepoListService.class)
                            .loadRepoList(accessToken, "pushed");

                    return call.execute().body().get(0).getPushDateLocalTime();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long lastPushTime) {
            if (lastPushTime != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(lastPushTime);

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                pushedTimeText.setText(year + "." + (month+1) + "." + day + " " + hour + ":" + minute);
                if (isTimeOver) {
                    if (pref.getLong("startDate", 0) > lastPushTime) {
                        lockImage.setImageResource(R.drawable.lock_image);
                        extraTimeText.setText("Time Over!");
                        lockStrText.setText(lockStr);
                        isUnlockable = false;
                    } else {
                        AlarmSingleTon.getInstance().setNewAlarm();
                        lockImage.setImageResource(R.drawable.no_worked_lock_image);
                        setExtraTimeTextCurrent();
                        lockStrText.setText(lockStrWorked);
                        isUnlockable = true;
                    }
                } else {
                    if (pref.getLong("startDate", 0) > lastPushTime) {
                        lockImage.setImageResource(R.drawable.no_worked_lock_image);
                        setExtraTimeTextCurrent();
                        lockStrText.setText(lockStrNoWorked);
                        isUnlockable = true;
                    } else {
                        lockImage.setImageResource(R.drawable.worked_lock_image);
                        setExtraTimeTextCurrent();
                        lockStrText.setText(lockStrWorked);
                        isUnlockable = true;
                    }
                }

            } else {
                lockStrText.setText(lockStrNetwork);
                isNetworkFailed = true;
                isUnlockable = true;
            }

            addView();
        }
    }


    private void setExtraTimeTextCurrent() {
        int subMinute = (int) ((pref.getLong("nextDate", 0) - Calendar.getInstance().getTimeInMillis()) / (1000 * 60));
        int minute = subMinute % 60;
        int subHour = subMinute / 60;
        int hour = subHour % 24;
        int day = subHour / 24;
        extraTimeText.setText(day + "일 " + hour + "시간 " + minute + "분");
    }


    private PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    isPhoneIdle = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    isPhoneIdle = false;
                    if (isPhoneIdleFailed) new NetTask().execute();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    isPhoneIdle = false;
                    break;
            }
        }
    };


}
