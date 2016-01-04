package kr.spyec.workdevman.Activity.DialogActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import kr.spyec.workdevman.Activity.MainActivity;
import kr.spyec.workdevman.Define.CDLockType;
import kr.spyec.workdevman.Define.CDRequest;
import kr.spyec.workdevman.LockScreen.AlarmSingleTon;
import kr.spyec.workdevman.R;

public class SelectLockDayActivity extends AppCompatActivity {

    int day;
    int hour;
    int minute;

    int lockType;

    SharedPreferences pref;

    TextView daySelectText;
    TextView timeSelectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lock_day);


        daySelectText = (TextView) findViewById(R.id.text_day_select);
        timeSelectText = (TextView) findViewById(R.id.text_time_select);

        day = 1;
        hour = 19;
        minute = 0;

        daySelectText.setText(String.valueOf(day));
        timeSelectText.setText(String.format("%d시 %d분", hour, minute));


        lockType = getIntent().getIntExtra("lockType", CDLockType.NO_LOCK);

        pref = getSharedPreferences("data", MODE_PRIVATE);

        findViewById(R.id.btn_day_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLockDayActivity.this, DayPickerActivity.class);
                intent.putExtra("value", day);
                startActivityForResult(intent, CDRequest.DAY_PICKER);
            }
        });

        findViewById(R.id.btn_time_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectLockDayActivity.this, TimePickerActivity.class);
                intent.putExtra("hour", hour);
                intent.putExtra("minute", minute);
                startActivityForResult(intent, CDRequest.TIME_PICKER);
            }
        });

        findViewById(R.id.btn_apply_select_day).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.edit()
                        .putInt("day", day)
                        .putInt("hour", hour)
                        .putInt("minute", minute)
                        .apply();

                int lockType = getIntent().getIntExtra("lockType", CDLockType.NO_LOCK);
                if(lockType == CDLockType.REPO_LOCK) pref.edit().putString("repoName", getIntent().getStringExtra("repoName")).apply();

                if(!AlarmSingleTon.getInstance().isInited()) AlarmSingleTon.getInstance().init(SelectLockDayActivity.this);
                AlarmSingleTon.getInstance().setNewAlarm();

                MainActivity.settingLockDoneAndStartActivity(lockType);

                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CDRequest.DAY_PICKER:
                    day = data.getIntExtra("value", 0);
                    daySelectText.setText(String.valueOf(day));
                    break;
                case CDRequest.TIME_PICKER:
                    hour = data.getIntExtra("hour", 0);
                    minute = data.getIntExtra("minute", 0);
                    timeSelectText.setText(String.format("%d시 %d분", hour, minute));
                    break;
            }
        }
    }
}
