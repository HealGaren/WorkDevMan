package kr.spyec.workdevman.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kr.spyec.workdevman.R;

public class Tutorial1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);
        findViewById(R.id.btn_tutorial_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tutorial1Activity.this, Tutorial2Activity.class));
                finish();
            }
        });

    }
}
