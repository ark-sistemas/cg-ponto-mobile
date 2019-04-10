package senai.fatesg.com.cgponto.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

import dmax.dialog.SpotsDialog;
import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.common.GraphicOverlay;
import senai.fatesg.com.cgponto.common.RectOverlay;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class CameraActivity extends AppCompatActivity implements InitComponent {

    protected CameraView cameraView;
    protected GraphicOverlay graphicOverlay;
    protected Button btnPhoto;

    protected AlertDialog waitingDialog;
    protected SharedPreferences sp;
    protected SharedPreferences.Editor pEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        FirebaseApp.initializeApp(this);
        init();
        buttonEvent();
        cameraListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    public void init() {
        cameraView = findViewById(R.id.camera_view);
        graphicOverlay = findViewById(R.id.graphic_overlay);
        btnPhoto = findViewById(R.id.btn_take_pic);
        waitingDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Analisando")
                .setCancelable(Boolean.FALSE)
                .build();
    }

    private void buttonEvent(){
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.captureImage();
                graphicOverlay.clear();
            }
        });
    }

    private void cameraListener(){

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();

                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,
                        cameraView.getMeasuredWidth(), cameraView.getMeasuredHeight(), Boolean.FALSE);
                cameraView.stop();

                runFaceDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void runFaceDetector(Bitmap bitmap) {
        FirebaseApp.initializeApp(this);
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                        .build();

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                processFaceResult(firebaseVisionFaces);
                savePreferences(firebaseVisionFaces);
            }
        }).addOnFailureListener(e -> Toast.makeText(CameraActivity.this, e.getMessage(),
                Toast.LENGTH_LONG).show());
    }

    private void processFaceResult(List<FirebaseVisionFace> firebaseVisionFaces) {

        firebaseVisionFaces.forEach(face ->{
            Rect bounds = face.getBoundingBox();
            RectOverlay rect = new RectOverlay(graphicOverlay, bounds);
            graphicOverlay.add(rect);

        });

        new Handler().postDelayed(() -> {
            Intent i = new Intent(CameraActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 4000);

        waitingDialog.dismiss();
    }

    private void savePreferences(List<FirebaseVisionFace> firebaseVisionFaces){
        sp = this.getSharedPreferences("face", Context.MODE_PRIVATE);
        pEditor = sp.edit();


        firebaseVisionFaces.forEach(face ->{

            //Countours Lists
            List<FirebaseVisionPoint> faceCountours = face
                    .getContour(FirebaseVisionFaceContour.FACE).getPoints();

            List<FirebaseVisionPoint> leftEyebrowTopCountours = face
                    .getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).getPoints();

            List<FirebaseVisionPoint> leftEyebrowBottomCountours = face
                    .getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).getPoints();

            List<FirebaseVisionPoint> rightEyebrowTopCountours = face
                    .getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).getPoints();

            List<FirebaseVisionPoint> rightEyebrowBottomCountours = face
                    .getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).getPoints();

            List<FirebaseVisionPoint> leftEyeCountours = face
                    .getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();

            List<FirebaseVisionPoint> rightEyeCountours = face
                    .getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints();

            List<FirebaseVisionPoint> noseBottomCountours = face
                    .getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).getPoints();

            List<FirebaseVisionPoint> noseBridgeCountours = face
                    .getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).getPoints();

            List<FirebaseVisionPoint> upperLipTopCountours = face
                    .getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).getPoints();

            List<FirebaseVisionPoint> upperLipBottomCountours = face
                    .getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();

            List<FirebaseVisionPoint> lowerLipTopCountours = face
                    .getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).getPoints();

            List<FirebaseVisionPoint> lowerLipBottomCountours = face
                    .getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM).getPoints();

            //Lists forEach
            faceCountours.forEach(faceCountour ->{
                String keyX = getString(R.string.face_countours_X)
                        + String.valueOf(faceCountours.indexOf(faceCountour));
                String keyY = getString(R.string.face_countours_Y)
                        + String.valueOf(faceCountours.indexOf(faceCountour));

                pEditor.putFloat(keyX, faceCountour.getX());
                pEditor.putFloat(keyY, faceCountour.getY());

            });

            leftEyebrowTopCountours.forEach(leftEyebrowTopCountour ->{

                String keyX = getString(R.string.left_eyebrow_top_countours_X)
                        + String.valueOf(leftEyebrowTopCountours.indexOf(leftEyebrowTopCountour));
                String keyY = getString(R.string.left_eyebrow_top_countours_Y)
                        + String.valueOf(leftEyebrowTopCountours.indexOf(leftEyebrowTopCountour));

                pEditor.putFloat(keyX, leftEyebrowTopCountour.getX());
                pEditor.putFloat(keyY, leftEyebrowTopCountour.getY());
            });

            leftEyebrowBottomCountours.forEach(leftEyebrowBottomCountour ->{

                String keyX = getString(R.string.left_eyebrow_bottom_countours_X)
                        + String.valueOf(leftEyebrowBottomCountours.indexOf(leftEyebrowBottomCountour));
                String keyY = getString(R.string.left_eyebrow_bottom_countours_Y)
                        + String.valueOf(leftEyebrowBottomCountours.indexOf(leftEyebrowBottomCountour));

                pEditor.putFloat(keyX, leftEyebrowBottomCountour.getX());
                pEditor.putFloat(keyY, leftEyebrowBottomCountour.getY());
            });

            rightEyebrowTopCountours.forEach(rightEyebrowTopCountour ->{

                String keyX = getString(R.string.right_eyebrow_top_countours_X)
                        + String.valueOf(rightEyebrowTopCountours.indexOf(rightEyebrowTopCountour));
                String keyY = getString(R.string.right_eyebrow_top_countours_Y)
                        + String.valueOf(rightEyebrowTopCountours.indexOf(rightEyebrowTopCountour));

                pEditor.putFloat(keyX, rightEyebrowTopCountour.getX());
                pEditor.putFloat(keyY, rightEyebrowTopCountour.getY());
            });

            rightEyebrowBottomCountours.forEach(rightEyebrowBottomCountour ->{

                String keyX = getString(R.string.right_eyebrow_bottom_countours_X)
                        + String.valueOf(rightEyebrowBottomCountours.indexOf(rightEyebrowBottomCountour));
                String keyY = getString(R.string.right_eyebrow_bottom_countours_Y)
                        + String.valueOf(rightEyebrowBottomCountours.indexOf(rightEyebrowBottomCountour));

                pEditor.putFloat(keyX, rightEyebrowBottomCountour.getX());
                pEditor.putFloat(keyY, rightEyebrowBottomCountour.getY());
            });

            leftEyeCountours.forEach(leftEyeCountour ->{

                String keyX = getString(R.string.left_eye_countours_X)
                        + String.valueOf(leftEyeCountours.indexOf(leftEyeCountour));
                String keyY = getString(R.string.left_eye_countours_Y)
                        + String.valueOf(leftEyeCountours.indexOf(leftEyeCountour));

                pEditor.putFloat(keyX, leftEyeCountour.getX());
                pEditor.putFloat(keyY, leftEyeCountour.getY());
            });

            rightEyeCountours.forEach(rightEyeCountour ->{

                String keyX = getString(R.string.right_eye_countours_X)
                        + String.valueOf(rightEyeCountours.indexOf(rightEyeCountour));
                String keyY = getString(R.string.right_eye_countours_Y)
                        + String.valueOf(rightEyeCountours.indexOf(rightEyeCountour));

                pEditor.putFloat(keyX, rightEyeCountour.getX());
                pEditor.putFloat(keyY, rightEyeCountour.getY());
            });

            noseBottomCountours.forEach(noseBottomCountour ->{
                String keyX = getString(R.string.nose_bottom_countours_X)
                        + String.valueOf(noseBottomCountours.indexOf(noseBottomCountour));
                String keyY = getString(R.string.nose_bottom_countours_Y)
                        + String.valueOf(noseBottomCountours.indexOf(noseBottomCountour));

                pEditor.putFloat(keyX, noseBottomCountour.getX());
                pEditor.putFloat(keyY, noseBottomCountour.getY());
            });

            noseBridgeCountours.forEach(noseBridgeCountour ->{
                String keyX = getString(R.string.nose_bridge_countours_X)
                        + String.valueOf(noseBridgeCountours.indexOf(noseBridgeCountour));
                String keyY = getString(R.string.nose_bridge_countours_Y)
                        + String.valueOf(noseBridgeCountours.indexOf(noseBridgeCountour));

                pEditor.putFloat(keyX, noseBridgeCountour.getX());
                pEditor.putFloat(keyY, noseBridgeCountour.getY());
            });

            upperLipTopCountours.forEach(upperLipTopCountour ->{
                String keyX = getString(R.string.upper_lip_top_countours_X)
                        + String.valueOf(upperLipTopCountours.indexOf(upperLipTopCountour));
                String keyY = getString(R.string.upper_lip_top_countours_Y)
                        + String.valueOf(upperLipTopCountours.indexOf(upperLipTopCountour));

                pEditor.putFloat(keyX, upperLipTopCountour.getX());
                pEditor.putFloat(keyY, upperLipTopCountour.getY());
            });

            upperLipBottomCountours.forEach(upperLipBottomCountour ->{
                String keyX = getString(R.string.upper_lip_bottom_countours_X)
                        + String.valueOf(upperLipBottomCountours.indexOf(upperLipBottomCountour));
                String keyY = getString(R.string.upper_lip_bottom_countours_Y)
                        + String.valueOf(upperLipBottomCountours.indexOf(upperLipBottomCountour));

                pEditor.putFloat(keyX, upperLipBottomCountour.getX());
                pEditor.putFloat(keyY, upperLipBottomCountour.getY());
            });

            lowerLipTopCountours.forEach(lowerLipTopCountour ->{
                String keyX = getString(R.string.lower_lip_top_countours_X)
                        + String.valueOf(lowerLipTopCountours.indexOf(lowerLipTopCountour));
                String keyY = getString(R.string.lower_lip_top_countours_Y)
                        + String.valueOf(lowerLipTopCountours.indexOf(lowerLipTopCountour));

                pEditor.putFloat(keyX, lowerLipTopCountour.getX());
                pEditor.putFloat(keyY, lowerLipTopCountour.getY());
            });

            lowerLipBottomCountours.forEach(lowerLipBottomCountour ->{
                String keyX = getString(R.string.lower_lip_bottom_countours_X)
                        + String.valueOf(lowerLipBottomCountours.indexOf(lowerLipBottomCountour));
                String keyY = getString(R.string.lower_lip_bottom_countours_Y)
                        + String.valueOf(lowerLipBottomCountours.indexOf(lowerLipBottomCountour));

                pEditor.putFloat(keyX, lowerLipBottomCountour.getX());
                pEditor.putFloat(keyY, lowerLipBottomCountour.getY());
            });
        });

        pEditor.apply();
    }
}
