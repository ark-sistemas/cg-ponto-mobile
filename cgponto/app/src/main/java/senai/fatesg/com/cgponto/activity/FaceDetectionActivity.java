package senai.fatesg.com.cgponto.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.List;
import java.util.Optional;

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class FaceDetectionActivity extends AppCompatActivity implements InitComponent {


    protected ImageView imageViewFaceDetec;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        init();
    }


    @Override
    public void init() {
        sp = getSharedPreferences("face", Context.MODE_PRIVATE);
        imageViewFaceDetec = findViewById(R.id.face_detection_camera_image_view);
    }

}
