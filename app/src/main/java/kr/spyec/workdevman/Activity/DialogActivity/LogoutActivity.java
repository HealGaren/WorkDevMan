package kr.spyec.workdevman.Activity.DialogActivity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.spyec.workdevman.R;

public class LogoutActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        pref = getSharedPreferences("data", MODE_PRIVATE);
        findViewById(R.id.btn_cancel_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.btn_apply_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().remove("accessToken").apply();
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
