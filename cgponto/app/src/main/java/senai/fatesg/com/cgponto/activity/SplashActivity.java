package senai.fatesg.com.cgponto.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import senai.fatesg.com.cgponto.R;

public class SplashActivity extends AppCompatActivity {

    private static final int TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(i);
            finish();
        }, TIME_OUT);
    }
}
