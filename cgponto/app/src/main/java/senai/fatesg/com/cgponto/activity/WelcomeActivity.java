package senai.fatesg.com.cgponto.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.adapter.MyPageAdapter;

public class WelcomeActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }

        addSliders();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startMainActivity();
    }

    private boolean isFirstTimeStartApp(){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("cgponto", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("FirstTimeStartFlag", Boolean.TRUE);
    }

    private void setFirstTimeStartStatus(Boolean start){
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("cgponto", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("FirstTimeStartFlag", start);
        editor.apply();
    }

    private void startMainActivity(){
        setFirstTimeStartStatus(Boolean.FALSE);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }



    private void addSliders(){

        //Login slider
        addSlide(AppIntroFragment.newInstance(null, null,
                getResources().getString(R.string.label_slide_login),
                null,
                R.drawable.login_password,
                getResources().getColor(R.color.backgroundColor),
                getResources().getColor(R.color.textColor),
                getResources().getColor(R.color.textColor)));

        // Face picture slider
        addSlide(AppIntroFragment.newInstance(null, null,
                getResources().getString(R.string.label_slide_fc_pic),
                null,
                R.drawable.camera,
                getResources().getColor(R.color.backgroundColor),
                getResources().getColor(R.color.textColor),
                getResources().getColor(R.color.textColor)));

        // Face recognition slider
        addSlide(AppIntroFragment.newInstance(null, null,
                getResources().getString(R.string.label_slide_recognition),
                null,
                R.drawable.fc_recognition,
                getResources().getColor(R.color.backgroundColor),
                getResources().getColor(R.color.textColor),
                getResources().getColor(R.color.textColor)));

        showSkipButton(true);
        setProgressButtonEnabled(true);
        setFadeAnimation();
//        setZoomAnimation();
//        setFlowAnimation();

//        setDepthAnimation();
    }
}
