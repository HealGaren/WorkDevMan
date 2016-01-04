package kr.spyec.workdevman.Activity.LoginActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.spyec.workdevman.Activity.DialogActivity.LoginWebActivity;
import kr.spyec.workdevman.Define.CDRequest;
import kr.spyec.workdevman.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CDRequest.LOGIN_WEB) {
            if(resultCode == RESULT_OK) {
                startActivity(data);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, LoginWebActivity.class), CDRequest.LOGIN_WEB);
            }
        });
    }


}
