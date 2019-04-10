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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Frame;
import com.otaliastudios.cameraview.FrameProcessor;

import java.util.List;
import java.util.Optional;

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class FaceDetectionActivity extends AppCompatActivity implements FrameProcessor, InitComponent {

    protected Facing facing = Facing.FRONT;

    protected ImageView imageViewFaceDetec;
    protected CameraView cameraView;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        init();
        settingCameraView();
    }

    @Override
    public void process(@NonNull Frame frame) {

        Optional<Integer> optionalWidth = Optional.ofNullable(frame.getSize().getWidth());

        Integer width = optionalWidth.get();
        Integer height = frame.getSize().getHeight();

        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setWidth(width)
                .setHeight(height)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation((cameraView.getFacing() == Facing.FRONT) ?
                        FirebaseVisionImageMetadata.ROTATION_270 :
                        FirebaseVisionImageMetadata.ROTATION_90)
                .build();

        FirebaseVisionImage visionImage = FirebaseVisionImage
                .fromByteArray(frame.getData(), metadata);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .build();

        FirebaseVisionFaceDetector faceDetector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        faceDetector.detectInImage(visionImage).addOnSuccessListener(firebaseVisionFaces -> {

            firebaseVisionFaces.forEach(face ->{

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


                faceCountours.forEach(faceCountour ->{
                    String keyX = getString(R.string.face_countours_X)
                            + String.valueOf(faceCountours.indexOf(faceCountour));
                    String keyY = getString(R.string.face_countours_Y)
                            + String.valueOf(faceCountours.indexOf(faceCountour));

                    if((sp.getFloat(keyX, 1f) == faceCountour.getX())
                        && sp.getFloat(keyY, 1f) == faceCountour.getY()){
                        Toast.makeText(this, "Igual", Toast.LENGTH_LONG).show();
                    }

                });

//                leftEyebrowTopCountours.forEach(leftEyebrowTopCountour ->{
//
//                    String keyX = R.string.left_eyebrow_top_countours_X
//                            + String.valueOf(leftEyebrowTopCountours.indexOf(leftEyebrowTopCountour));
//                    String keyY = R.string.left_eyebrow_top_countours_Y
//                            + String.valueOf(leftEyebrowTopCountours.indexOf(leftEyebrowTopCountour));
//
//                });
//
//                leftEyebrowBottomCountours.forEach(leftEyebrowBottomCountour ->{
//
//                    String keyX = R.string.left_eyebrow_bottom_countours_X
//                            + String.valueOf(leftEyebrowBottomCountours.indexOf(leftEyebrowBottomCountour));
//                    String keyY = R.string.left_eyebrow_bottom_countours_Y
//                            + String.valueOf(leftEyebrowBottomCountours.indexOf(leftEyebrowBottomCountour));
//
//                });
//
//                rightEyebrowTopCountours.forEach(rightEyebrowTopCountour ->{
//
//                    String keyX = R.string.right_eyebrow_top_countours_X
//                            + String.valueOf(rightEyebrowTopCountours.indexOf(rightEyebrowTopCountour));
//                    String keyY = R.string.right_eyebrow_top_countours_Y
//                            + String.valueOf(rightEyebrowTopCountours.indexOf(rightEyebrowTopCountour));
//
//                });
//
//                rightEyebrowBottomCountours.forEach(rightEyebrowBottomCountour ->{
//
//                    String keyX = R.string.right_eyebrow_bottom_countours_X
//                            + String.valueOf(rightEyebrowBottomCountours.indexOf(rightEyebrowBottomCountour));
//                    String keyY = R.string.right_eyebrow_bottom_countours_Y
//                            + String.valueOf(rightEyebrowBottomCountours.indexOf(rightEyebrowBottomCountour));
//
//                });
//
//                leftEyeCountours.forEach(leftEyeCountour ->{
//
//                    String keyX = R.string.left_eye_countours_X
//                            + String.valueOf(leftEyeCountours.indexOf(leftEyeCountour));
//                    String keyY = R.string.left_eye_countours_Y
//                            + String.valueOf(leftEyeCountours.indexOf(leftEyeCountour));
//
//                });
//
//                rightEyeCountours.forEach(rightEyeCountour ->{
//
//                    String keyX = R.string.right_eye_countours_X
//                            + String.valueOf(rightEyeCountours.indexOf(rightEyeCountour));
//                    String keyY = R.string.right_eye_countours_Y
//                            + String.valueOf(rightEyeCountours.indexOf(rightEyeCountour));
//
//                });
//
//                noseBottomCountours.forEach(noseBottomCountour ->{
//                    String keyX = R.string.nose_bottom_countours_X
//                            + String.valueOf(noseBottomCountours.indexOf(noseBottomCountour));
//                    String keyY = R.string.nose_bottom_countours_Y
//                            + String.valueOf(noseBottomCountours.indexOf(noseBottomCountour));
//
//                });
//
//                noseBridgeCountours.forEach(noseBridgeCountour ->{
//                    String keyX = R.string.nose_bridge_countours_X
//                            + String.valueOf(noseBridgeCountours.indexOf(noseBridgeCountour));
//                    String keyY = R.string.nose_bridge_countours_Y
//                            + String.valueOf(noseBridgeCountours.indexOf(noseBridgeCountour));
//
//                });
//
//                upperLipTopCountours.forEach(upperLipTopCountour ->{
//                    String keyX = R.string.upper_lip_top_countours_X
//                            + String.valueOf(upperLipTopCountours.indexOf(upperLipTopCountour));
//                    String keyY = R.string.upper_lip_top_countours_Y
//                            + String.valueOf(upperLipTopCountours.indexOf(upperLipTopCountour));
//
//                });
//
//                upperLipBottomCountours.forEach(upperLipBottomCountour ->{
//                    String keyX = R.string.upper_lip_bottom_countours_X
//                            + String.valueOf(upperLipBottomCountours.indexOf(upperLipBottomCountour));
//                    String keyY = R.string.upper_lip_bottom_countours_Y
//                            + String.valueOf(upperLipBottomCountours.indexOf(upperLipBottomCountour));
//
//                });
//
//                lowerLipTopCountours.forEach(lowerLipTopCountour ->{
//                    String keyX = R.string.lower_lip_top_countours_X
//                            + String.valueOf(lowerLipTopCountours.indexOf(lowerLipTopCountour));
//                    String keyY = R.string.lower_lip_top_countours_Y
//                            + String.valueOf(lowerLipTopCountours.indexOf(lowerLipTopCountour));
//
//                });
//
//                lowerLipBottomCountours.forEach(lowerLipBottomCountour ->{
//                    String keyX = R.string.lower_lip_bottom_countours_X
//                            + String.valueOf(lowerLipBottomCountours.indexOf(lowerLipBottomCountour));
//                    String keyY = R.string.lower_lip_bottom_countours_Y
//                            + String.valueOf(lowerLipBottomCountours.indexOf(lowerLipBottomCountour));
//
//                });
            });

//            imageViewFaceDetec.setImageBitmap(null);
//            Bitmap bitmap = Bitmap.createBitmap(width, height,
//                    Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//
//            //Face
//            Paint facePaint = new Paint();
//            facePaint.setColor(Color.RED);
//            facePaint.setStyle(Paint.Style.STROKE);
//            facePaint.setStrokeWidth(4f);
//
//            firebaseVisionFaces.forEach(face ->{
//
//                canvas.drawRect(face.getBoundingBox(), facePaint);
//
//
//                if(cameraView.getFacing() == Facing.FRONT){
//                    Matrix matrix = new Matrix();
//                    matrix.preScale(-1f, 1f);
//                    Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                            bitmap.getHeight(), matrix, Boolean.TRUE);
//
//                    imageViewFaceDetec.setImageBitmap(flippedBitmap);
//                } else {
//                    imageViewFaceDetec.setImageBitmap(bitmap);
//                }
//            });
        }).addOnFailureListener(e -> {
            imageViewFaceDetec.setImageBitmap(null);

        });
    }

    @Override
    public void init() {
        sp = getSharedPreferences("face", Context.MODE_PRIVATE);
        imageViewFaceDetec = findViewById(R.id.face_detection_camera_image_view);
        cameraView = findViewById(R.id.face_detection_camera_view);
    }

    private void settingCameraView(){
        cameraView.setFacing(facing);
        cameraView.setLifecycleOwner(this);
        cameraView.addFrameProcessor(this);
    }
}
