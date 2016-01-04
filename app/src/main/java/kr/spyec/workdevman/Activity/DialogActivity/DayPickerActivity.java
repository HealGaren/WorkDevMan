package kr.spyec.workdevman.Activity.DialogActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;

import kr.spyec.workdevman.R;

public class DayPickerActivity extends AppCompatActivity {

    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_picker);

        numberPicker = (NumberPicker) findViewById(R.id.number_picker);

        Intent i = getIntent();


        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(1);
        numberPicker.setValue(i.getIntExtra("value", 1));

        findViewById(R.id.btn_apply_number_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("value", numberPicker.getValue());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
