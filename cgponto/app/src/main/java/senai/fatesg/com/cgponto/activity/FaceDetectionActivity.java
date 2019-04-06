package senai.fatesg.com.cgponto.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

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

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class FaceDetectionActivity extends AppCompatActivity implements FrameProcessor, InitComponent {

    Facing facing = Facing.FRONT;

    ImageView imageViewFaceDetec;
    CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detection);
        init();
        settingCameraView();
    }

    @Override
    public void process(@NonNull Frame frame) {

        Integer width = frame.getSize().getWidth();
        Integer height = frame.getSize().getHeight();

        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setWidth(width)
                .setHeight(height)
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation((cameraView.getFacing() == Facing.FRONT) ? FirebaseVisionImageMetadata.ROTATION_270 : FirebaseVisionImageMetadata.ROTATION_90)
                .build();

        FirebaseVisionImage visionImage = FirebaseVisionImage.fromByteArray(frame.getData(), metadata);

        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions.Builder()
                .setContourMode(FirebaseVisionFaceDetectorOptions.ALL_CONTOURS)
                .build();

        FirebaseVisionFaceDetector faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options);

        faceDetector.detectInImage(visionImage).addOnSuccessListener(firebaseVisionFaces -> {

            imageViewFaceDetec.setImageBitmap(null);
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            //Dots
            Paint dotPaint = new Paint();
            dotPaint.setColor(Color.BLUE);
            dotPaint.setStyle(Paint.Style.FILL);
            dotPaint.setStrokeWidth(4f);

            //Lines
            Paint linePaint = new Paint();
            linePaint.setColor(Color.parseColor("#007fff"));
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(2f);

            firebaseVisionFaces.forEach(face ->{

                List<FirebaseVisionPoint> faceCountours = face.getContour(FirebaseVisionFaceContour.FACE)
                        .getPoints();

                faceCountours.forEach(countour -> {
                    if(faceCountours.indexOf(countour) != faceCountours.size() - 1){
                        canvas.drawLine(countour.getX(), countour.getY(),
                                faceCountours.get(faceCountours.indexOf(countour) + 1).getX(),
                                faceCountours.get(faceCountours.indexOf(countour) + 1).getY(),
                                linePaint);
                    } else {
                        canvas.drawLine(countour.getX(), countour.getY(),
                                faceCountours.get(0).getX(),
                                faceCountours.get(0).getY(),
                                linePaint);
                    }
                });

                List<FirebaseVisionPoint> leftEyebrowTopCountours = face
                        .getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_TOP).getPoints();
                leftEyebrowTopCountours.forEach(leftEyebrowTop ->{
                    if(leftEyebrowTopCountours.indexOf(leftEyebrowTop)
                            != leftEyebrowTopCountours.size() - 1){
                        canvas.drawLine(leftEyebrowTop.getX(), leftEyebrowTop.getY(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(leftEyebrowTop) + 1).getX(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(leftEyebrowTop) + 1).getY(),
                                linePaint);
                        canvas.drawCircle(leftEyebrowTop.getX(), leftEyebrowTop.getY(), 4f, dotPaint);
                    }
                });

                List<FirebaseVisionPoint> leftEyebrowBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).getPoints();
                leftEyebrowBottomCountours.forEach(leftEyebrowBottom ->{
                    if(leftEyebrowTopCountours.indexOf(leftEyebrowBottom)
                            != leftEyebrowTopCountours.size() - 1){
                        canvas.drawLine(leftEyebrowBottom.getX(), leftEyebrowBottom.getY(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(leftEyebrowBottom) + 1).getX(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(leftEyebrowBottom) + 1).getY(),
                                linePaint);
                        canvas.drawCircle(leftEyebrowBottom.getX(), leftEyebrowBottom.getY(), 4f, dotPaint);
                    }
                });

                List<FirebaseVisionPoint> rightEyebrowTopCountours = face
                        .getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).getPoints();
                rightEyebrowTopCountours.forEach(rightEyebrowTop ->{
                    if(leftEyebrowTopCountours.indexOf(rightEyebrowTop)
                            != leftEyebrowTopCountours.size() - 1){
                        canvas.drawLine(rightEyebrowTop.getX(), rightEyebrowTop.getY(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(rightEyebrowTop) + 1).getX(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(rightEyebrowTop) + 1).getY(),
                                linePaint);
                        canvas.drawCircle(rightEyebrowTop.getX(), rightEyebrowTop.getY(), 4f, dotPaint);
                    }
                });

                List<FirebaseVisionPoint> rightEyebrowBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).getPoints();
                rightEyebrowBottomCountours.forEach(rightEyebrowBottom ->{
                    if(leftEyebrowTopCountours.indexOf(rightEyebrowBottom)
                            != leftEyebrowTopCountours.size() - 1){
                        canvas.drawLine(rightEyebrowBottom.getX(), rightEyebrowBottom.getY(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(rightEyebrowBottom) + 1).getX(),
                                leftEyebrowTopCountours.get(leftEyebrowTopCountours.indexOf(rightEyebrowBottom) + 1).getY(),
                                linePaint);
                        canvas.drawCircle(rightEyebrowBottom.getX(), rightEyebrowBottom.getY(), 4f, dotPaint);
                    }
                });

                List<FirebaseVisionPoint> leftEyeCountours = face
                        .getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                leftEyeCountours.forEach(leftEyeCountour ->{
                    if(leftEyeCountours.indexOf(leftEyeCountour) !=
                        leftEyeCountours.size() - 1){

                    }
                });
            });
        });
    }

    @Override
    public void init() {
        imageViewFaceDetec = findViewById(R.id.face_detection_camera_image_view);
        cameraView = findViewById(R.id.face_detection_camera_view);
    }

    private void settingCameraView(){
        cameraView.setFacing(facing);
        cameraView.setLifecycleOwner(this);
        cameraView.addFrameProcessor(this);
    }
}
