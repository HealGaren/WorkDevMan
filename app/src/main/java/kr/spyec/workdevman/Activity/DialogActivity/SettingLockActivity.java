package kr.spyec.workdevman.Activity.DialogActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Switch;

import kr.spyec.workdevman.Activity.MainActivity;
import kr.spyec.workdevman.Define.CDLockType;
import kr.spyec.workdevman.R;

public class SettingLockActivity extends AppCompatActivity implements View.OnClickListener {

    Switch lockActiveSwitch;
    SharedPreferences pref;

    CardView setNoneCardView;
    CardView setRepoCardView;
    CardView setUserCardView;

    Animation cardAnim;
    Animation delayedCardAnim1;
    Animation delayedCardAnim2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_lock);

        pref = getSharedPreferences("data", MODE_PRIVATE);

        setNoneCardView = (CardView) findViewById(R.id.card_view_setting_lock_none);
        setRepoCardView = (CardView) findViewById(R.id.card_view_setting_lock_repo);
        setUserCardView = (CardView) findViewById(R.id.card_view_setting_lock_user);

        cardAnim = AnimationUtils.loadAnimation(SettingLockActivity.this, R.anim.create_card_view);
        delayedCardAnim1 = AnimationUtils.loadAnimation(SettingLockActivity.this, R.anim.create_card_view_delay_1);
        delayedCardAnim2 = AnimationUtils.loadAnimation(SettingLockActivity.this, R.anim.create_card_view_delay_2);

        setNoneCardView.setOnClickListener(this);
        setRepoCardView.setOnClickListener(this);
        setUserCardView.setOnClickListener(this);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setNoneCardView.startAnimation(cardAnim);
                setRepoCardView.startAnimation(delayedCardAnim1);
                setUserCardView.startAnimation(delayedCardAnim2);
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.card_view_setting_lock_repo:
                intent = new Intent(this, SelectRepoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.card_view_setting_lock_user:
                intent = new Intent(this, SelectLockDayActivity.class);
                intent.putExtra("lockType", CDLockType.USER_LOCK);
                startActivity(intent);
                finish();
                break;
            default:
                MainActivity.settingLockDoneAndStartActivity(CDLockType.NO_LOCK);
                finish();
        }

    }
}