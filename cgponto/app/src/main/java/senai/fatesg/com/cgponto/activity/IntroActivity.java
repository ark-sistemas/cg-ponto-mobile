package senai.fatesg.com.cgponto.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import senai.fatesg.com.cgponto.R;

public class IntroActivity extends AppCompatActivity {

    private static final int TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }
}
