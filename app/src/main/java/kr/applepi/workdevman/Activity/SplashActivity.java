package kr.applepi.workdevman.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kr.applepi.workdevman.Activity.LoginActivity.LoginActivity;
import kr.applepi.workdevman.R;
import kr.applepi.workdevman.ServiceTool;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = getSharedPreferences("data", MODE_PRIVATE);

        ServiceTool.reloadService(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (pref.getBoolean("isFirst", true)) {
                    intent = new Intent(SplashActivity.this, Tutorial1Activity.class);
                } else if (pref.getString("accessToken", "").equals("")) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
