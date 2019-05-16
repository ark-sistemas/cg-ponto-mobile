package senai.fatesg.com.cgponto.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;


import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class FaceDetectionActivity extends AppCompatActivity implements InitComponent {


    protected ImageView imageViewFaceDetec;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        initComponents();
    }


    @Override
    public void initComponents() {
        sp = getSharedPreferences("face", Context.MODE_PRIVATE);
        imageViewFaceDetec = findViewById(R.id.face_detection_camera_image_view);
    }

}
