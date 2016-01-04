package kr.spyec.workdevman.Activity.DialogActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import kr.spyec.workdevman.R;

public class TimePickerActivity extends AppCompatActivity {

    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        timePicker = (TimePicker) findViewById(R.id.time_picker);

        Intent i = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(i.getIntExtra("hour", 0));
            timePicker.setMinute(i.getIntExtra("minute", 0));
        }
        else {
            timePicker.setCurrentHour(i.getIntExtra("hour", 0));
            timePicker.setCurrentMinute(i.getIntExtra("minute", 0));
        }


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

            }
        });

        findViewById(R.id.btn_apply_time_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    intent.putExtra("hour", timePicker.getHour());
                    intent.putExtra("minute", timePicker.getMinute());
                }
                else {
                    intent.putExtra("hour", timePicker.getCurrentHour());
                    intent.putExtra("minute", timePicker.getCurrentMinute());
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
