package senai.fatesg.com.cgponto.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
//        sp = getSharedPreferences("face", Context.MODE_PRIVATE);
//        Toast.makeText(this, String.valueOf(sp.getFloat(R.string.face_countours_Y + "1",
//                1f)),
//                Toast.LENGTH_LONG).show();
        Intent i = new Intent(LoginActivity.this, FaceDetectionActivity.class);
        startActivity(i);
    }
}
