package senai.fatesg.com.cgponto.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.adapter.MyPageAdapter;

public class WelcomeActivity extends AppCompatActivity {

    protected Button btnNext;
    protected Button btnSkip;

    protected ViewPager viewPager;

    protected LinearLayout dotLayout;

    protected TextView[] dotsView;

    protected int[] layouts = new int[]{R.layout.slider_1, R.layout.slider_2, R.layout.slider_3};;

    protected MyPageAdapter myPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isFirstTimeStartApp()){
            startMainActivity();
            finish();
        }

        setContentView(R.layout.activity_welcome);
        init();

        myPageAdapter = new MyPageAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(myPageAdapter);
        pageListener();
        setDotStatus(0);
    }

    public void init(){

        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);
        viewPager = findViewById(R.id.view_pager);
        dotLayout = findViewById(R.id.dot_layout);

    }

    public void skip(View view) {
        startMainActivity();
        finish();
    }

    private void setDotStatus(int page){
        dotLayout.removeAllViews();
        dotsView = new TextView[layouts.length];
        for (int i = 0; i < dotsView.length; i++) {
            dotsView[i] = new TextView(this);
            dotsView[i].setText(Html.fromHtml("&#8226"));
            dotsView[i].setTextSize(30);
            dotsView[i].setTextColor(Color.parseColor("#a9b4bb"));
            dotLayout.addView(dotsView[i]);
        }

        if(dotsView.length > 0){
            dotsView[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void pageListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == layouts.length - 1){
                    btnNext.setText(R.string.btn_next_op_1);
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText(R.string.btn_next);
                    btnSkip.setVisibility(View.VISIBLE);
                }

                setDotStatus(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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

    public void next(View view) {
        int currentPage = viewPager.getCurrentItem() + 1;

        if(currentPage < layouts.length){
            viewPager.setCurrentItem(currentPage);
        } else {
            startMainActivity();
        }
    }

    private void startMainActivity(){
        setFirstTimeStartStatus(Boolean.FALSE);
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }
}
