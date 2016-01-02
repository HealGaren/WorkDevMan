package kr.applepi.workdevman.Activity.LoginActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.applepi.workdevman.Activity.MainActivity;
import kr.applepi.workdevman.R;

public class LoginDoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_done);

        findViewById(R.id.btn_callback_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginDoneActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
