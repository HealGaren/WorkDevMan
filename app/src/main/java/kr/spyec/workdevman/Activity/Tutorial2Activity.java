package kr.spyec.workdevman.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.spyec.workdevman.Activity.LoginActivity.LoginActivity;
import kr.spyec.workdevman.R;

public class Tutorial2Activity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);
        pref = getSharedPreferences("data", MODE_PRIVATE);
        findViewById(R.id.btn_tutorial_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().putBoolean("isFirst", false).apply();
                Intent intent;
                if(pref.getString("accessToken", "").equals("")) intent = new Intent(Tutorial2Activity.this, LoginActivity.class);
                else intent = new Intent(Tutorial2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
