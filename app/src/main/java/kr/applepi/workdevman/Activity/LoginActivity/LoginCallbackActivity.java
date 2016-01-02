package kr.applepi.workdevman.Activity.LoginActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import kr.applepi.workdevman.Define.CDServer;
import kr.applepi.workdevman.HttpService.OAuth.GitTokenData;
import kr.applepi.workdevman.HttpService.OAuth.GitTokenService;
import kr.applepi.workdevman.HttpService.NetServiceFactory;
import kr.applepi.workdevman.R;

/**
 * Created by qkswk on 2015-12-27.
 */

public class LoginCallbackActivity extends AppCompatActivity {

    public static final int RESULT_OAUTH_OK = -13;
    public static final int RESULT_OAUTH_ERROR = -14;


    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_callback);

        pref = getSharedPreferences("data", MODE_PRIVATE);

        new NetTask().execute(getIntent().getStringExtra("code"));

    }




    private class NetTask extends AsyncTask<String, Void, GitTokenData> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(LoginCallbackActivity.this, "", "토큰을 발급하는 중입니다...", true);
        }

        @Override
        protected GitTokenData doInBackground(String... params) {
            try {
                return NetServiceFactory.createHomeService(GitTokenService.class)
                        .loadTokenData(CDServer.CLIENT_ID, CDServer.CLIENT_SECRET, params[0])
                        .execute()
                        .body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GitTokenData gitTokenData) {
            pref.edit()
                    .putString("accessToken", gitTokenData.getAccessToken())
                    .putString("tokenType", gitTokenData.getTokenType())
                    .apply();
            mProgressDialog.dismiss();
            startActivity(new Intent(LoginCallbackActivity.this, LoginDoneActivity.class));
            finish();
        }
    }
}
