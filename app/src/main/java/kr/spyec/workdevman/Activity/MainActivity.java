package kr.spyec.workdevman.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.io.IOException;

import kr.spyec.workdevman.Activity.DialogActivity.LogoutActivity;
import kr.spyec.workdevman.Activity.DialogActivity.SettingLockActivity;
import kr.spyec.workdevman.Activity.DialogActivity.SuccessActivity;
import kr.spyec.workdevman.Activity.LoginActivity.LoginActivity;
import kr.spyec.workdevman.Define.CDLockType;
import kr.spyec.workdevman.Define.CDRequest;
import kr.spyec.workdevman.HttpService.API.GitUserData;
import kr.spyec.workdevman.HttpService.API.GitUserDataService;
import kr.spyec.workdevman.HttpService.NetServiceFactory;
import kr.spyec.workdevman.R;
import kr.spyec.workdevman.Transforms;
import retrofit.Call;

public class MainActivity extends AppCompatActivity {

    LayoutInflater inflater;

    LinearLayout mainLayout;

    CardView profileCardView;
    ImageView gitProfileImage;
    TextView gitUsernameText;


    CardView lockSettingCardView;
    TextView lockTypeText;

    CardView graphCardView;

    CardView messageCardView;

    Animation cardAnim;
    Animation delayedCardAnim;
    Animation delayed2CardAnim;
    Animation delayed3CardAnim;

    SharedPreferences pref;

    String accessToken;


    private static MainActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        inflater = getLayoutInflater();

        mainLayout = (LinearLayout) findViewById(R.id.layout_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        CollapsingToolbarLayout ctLayout = ((CollapsingToolbarLayout) findViewById(R.id.collapsing));
        ctLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.colorPrimary) & 0x00ffffff);

        cardAnim = AnimationUtils.loadAnimation(this, R.anim.create_card_view);
        delayedCardAnim = AnimationUtils.loadAnimation(this, R.anim.create_card_view_delay_1);
        delayed2CardAnim = AnimationUtils.loadAnimation(this, R.anim.create_card_view_delay_2);
        delayed3CardAnim = AnimationUtils.loadAnimation(this, R.anim.create_card_view_delay_3);

        pref = getSharedPreferences("data", MODE_PRIVATE);

        accessToken = pref.getString("accessToken", "");
        createProfileCardView();
        createLockSettingCardView();
        createGraphCardView();
        createMessageCardView();

        new NetTask().execute();

    }

    public void createProfileCardView() {

        profileCardView = (CardView) inflater.inflate(R.layout.card_view_git_profile, mainLayout, false);

        gitProfileImage = (ImageView) profileCardView.findViewById(R.id.image_git_profile);
        gitUsernameText = (TextView) profileCardView.findViewById(R.id.text_git_username);
        profileCardView.findViewById(R.id.btn_setting_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, LogoutActivity.class), CDRequest.LOGOUT);
            }
        });

    }

    public void createLockSettingCardView() {
        lockSettingCardView = (CardView) inflater.inflate(R.layout.card_view_lock_setting, mainLayout, false);

        lockTypeText = (TextView) lockSettingCardView.findViewById(R.id.text_lock_type);

        lockSettingCardView.findViewById(R.id.btn_setting_lock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingLockActivity.class));
            }
        });
    }

    public void createGraphCardView(){
        graphCardView = (CardView) inflater.inflate(R.layout.card_view_graph, mainLayout, false);
        graphCardView.findViewById(R.id.btn_none).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void createMessageCardView(){
        messageCardView = (CardView) inflater.inflate(R.layout.card_view_message, mainLayout, false);
        messageCardView.findViewById(R.id.btn_facebook_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/100005247008742")));
                } catch(Exception e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.katana")));
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case CDRequest.LOGOUT:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
                break;
            case CDRequest.SETTING_LOCK_DONE:
                new NetTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        for (int i = 0; i < menu.size(); i++) menu.getItem(i).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NetTask extends AsyncTask<Void, Void, GitUserData> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(MainActivity.this, "", "서버에서 정보를 받아오는 중입니다...", true);
            if(profileCardView.getParent() != null) mainLayout.removeView(profileCardView);
            if(lockSettingCardView.getParent() != null) mainLayout.removeView(lockSettingCardView);
            if(graphCardView.getParent() != null) mainLayout.removeView(graphCardView);
            if(messageCardView.getParent() != null) mainLayout.removeView(messageCardView);
        }

        @Override
        protected GitUserData doInBackground(Void... params) {

            Call<GitUserData> call = NetServiceFactory.createAPIService(GitUserDataService.class).loadUserData(accessToken);
            try {
                return call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(GitUserData gitUserData) {

            if(gitUserData == null) {
                Snackbar.make(mainLayout, "GitHub 데이터를 받아오지 못했습니다.\n네트워크 상태를 확인해주세요.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else {

                pref.edit().putString("username", gitUserData.getUsername()).apply();

                Picasso.with(MainActivity.this)
                        .load(gitUserData.getImageUrl())
                        .placeholder(R.drawable.progress_animation)
                        .transform(Transforms.circleTransform)
                        .into(gitProfileImage);


                gitUsernameText.setText(gitUserData.getUsername());

                switch(pref.getInt("lockType", CDLockType.NO_LOCK)) {
                    case CDLockType.NO_LOCK:
                        lockTypeText.setText("No Lock");
                        break;
                    case CDLockType.REPO_LOCK:
                        lockTypeText.setText("Repository Lock");
                        break;
                    case CDLockType.USER_LOCK:
                        lockTypeText.setText("Account Lock");
                        break;
                }

                mainLayout.addView(profileCardView, 0);
                profileCardView.startAnimation(cardAnim);
                mainLayout.addView(lockSettingCardView, 1);
                lockSettingCardView.startAnimation(delayedCardAnim);
                mainLayout.addView(graphCardView, 2);
                graphCardView.startAnimation(delayed2CardAnim);
                mainLayout.addView(messageCardView, 3);
                messageCardView.startAnimation(delayed3CardAnim);

            }
            mProgressDialog.dismiss();

        }
    }

    public static void settingLockDoneAndStartActivity(int lockType){
        instance.pref.edit().putInt("lockType", lockType).apply();
        instance.startActivityForResult(new Intent(instance, SuccessActivity.class), CDRequest.SETTING_LOCK_DONE);
    }
}
