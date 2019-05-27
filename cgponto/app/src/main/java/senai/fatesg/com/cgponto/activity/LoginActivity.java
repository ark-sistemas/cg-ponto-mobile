package senai.fatesg.com.cgponto.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import senai.fatesg.com.cgponto.R;

public class LoginActivity extends AppCompatActivity {

    protected TextView txtForgotPassword;

    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void recoverPasswd(View view) {

        txtForgotPassword = findViewById(R.id.txtForgotPassword);

        Intent i = new Intent(LoginActivity.this, ForgotPasswdActivity.class);
        startActivity(i);
    }

    public void login(View view) {
        Intent i = new Intent(LoginActivity.this, FaceRecognitionAppActivity.class);
//        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(i);
    }
}
