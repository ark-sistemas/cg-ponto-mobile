package senai.fatesg.com.cgponto.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import senai.fatesg.com.cgponto.R;

public class LoginActivity extends AppCompatActivity {

    TextView txtForgotPassword;
    Button btnEntrar;

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

    public void entrar(View view) {

        Intent i = new Intent(LoginActivity.this, AbonoActivity.class);
        startActivity(i);
    }
}
