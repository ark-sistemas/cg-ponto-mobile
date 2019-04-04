package senai.fatesg.com.cgponto.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import senai.fatesg.com.cgponto.R;

public class WelcomeActivity extends AppCompatActivity {

    protected Button btnNext;
    protected Button btnSkip;

    protected ViewPager viewPager;

    protected LinearLayout dotLayout;

    protected TextView[] dotsView;

    protected int[] layouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    public void init(){

        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);
        viewPager = findViewById(R.id.view_pager);
        dotLayout = findViewById(R.id.dot_layout);

    }
}
