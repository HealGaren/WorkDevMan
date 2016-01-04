package kr.spyec.workdevman.Activity.DialogActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kr.spyec.workdevman.Activity.LoginActivity.LoginCallbackActivity;
import kr.spyec.workdevman.Define.CDServer;
import kr.spyec.workdevman.R;

public class LoginWebActivity extends AppCompatActivity {

    WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_web);

        web = (WebView) findViewById(R.id.web_view_login);

        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(CDServer.BASE_HOME_URL + "/login/oauth/authorize" + "?client_id=" + CDServer.CLIENT_ID);
        web.setWebViewClient(new WebViewClient() {

            Intent intent = new Intent(LoginWebActivity.this, LoginCallbackActivity.class);


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url.contains("?code=")) {
                    Uri uri = Uri.parse(url);
                    intent.putExtra("code", uri.getQueryParameter("code"));
                    setResult(RESULT_OK, intent);
                    finish();

                }
            }
        });
    }
}
