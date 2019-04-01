package senai.fatesg.com.cgponto.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import senai.fatesg.com.cgponto.R;

public class LoginActivity extends AppCompatActivity {

    TextView txtForgotPassword;

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
        Intent i = new Intent(LoginActivity.this, CameraActivity.class);
        startActivity(i);
    }
}
