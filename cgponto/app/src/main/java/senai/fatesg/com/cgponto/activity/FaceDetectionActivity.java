package senai.fatesg.com.cgponto.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

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

            imageViewFaceDetec.setImageBitmap(null);
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            //Dots
            Paint dotPaint = new Paint();
            dotPaint.setColor(Color.YELLOW);
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
                faceCountours.forEach(faceCountour -> {
                    if(faceCountours.indexOf(faceCountour) != faceCountours.size() - 1){
                        canvas.drawLine(faceCountour.getX(), faceCountour.getY(),
                                faceCountours.get(faceCountours.indexOf(faceCountour) + 1).getX(),
                                faceCountours.get(faceCountours.indexOf(faceCountour) + 1).getY(),
                                linePaint);
                    } else {
                        canvas.drawLine(faceCountour.getX(), faceCountour.getY(),
                                faceCountours.get(0).getX(),
                                faceCountours.get(0).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(faceCountour.getX(), faceCountour.getY(), 4f,
                            dotPaint);
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
                    }
                    canvas.drawCircle(leftEyebrowTop.getX(), leftEyebrowTop.getY(), 4f, dotPaint);
                });

                List<FirebaseVisionPoint> leftEyebrowBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.LEFT_EYEBROW_BOTTOM).getPoints();
                leftEyebrowBottomCountours.forEach(leftEyebrowBottom ->{
                    if(leftEyebrowBottomCountours.indexOf(leftEyebrowBottom)
                            != leftEyebrowBottomCountours.size() - 1){
                        canvas.drawLine(leftEyebrowBottom.getX(), leftEyebrowBottom.getY(),
                                leftEyebrowBottomCountours.get(leftEyebrowBottomCountours.indexOf(leftEyebrowBottom) + 1).getX(),
                                leftEyebrowBottomCountours.get(leftEyebrowBottomCountours.indexOf(leftEyebrowBottom) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(leftEyebrowBottom.getX(), leftEyebrowBottom.getY(), 4f, dotPaint);
                });

                List<FirebaseVisionPoint> rightEyebrowTopCountours = face
                        .getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_TOP).getPoints();
                rightEyebrowTopCountours.forEach(rightEyebrowTop ->{
                    if(rightEyebrowTopCountours.indexOf(rightEyebrowTop)
                            != rightEyebrowTopCountours.size() - 1){
                        canvas.drawLine(rightEyebrowTop.getX(), rightEyebrowTop.getY(),
                                rightEyebrowTopCountours.get(rightEyebrowTopCountours.indexOf(rightEyebrowTop) + 1).getX(),
                                rightEyebrowTopCountours.get(rightEyebrowTopCountours.indexOf(rightEyebrowTop) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(rightEyebrowTop.getX(), rightEyebrowTop.getY(), 4f, dotPaint);
                });

                List<FirebaseVisionPoint> rightEyebrowBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.RIGHT_EYEBROW_BOTTOM).getPoints();
                rightEyebrowBottomCountours.forEach(rightEyebrowBottom ->{
                    if(rightEyebrowBottomCountours.indexOf(rightEyebrowBottom)
                            != rightEyebrowBottomCountours.size() - 1){
                        canvas.drawLine(rightEyebrowBottom.getX(), rightEyebrowBottom.getY(),
                                rightEyebrowBottomCountours.get(rightEyebrowBottomCountours.indexOf(rightEyebrowBottom) + 1).getX(),
                                rightEyebrowBottomCountours.get(rightEyebrowBottomCountours.indexOf(rightEyebrowBottom) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(rightEyebrowBottom.getX(), rightEyebrowBottom.getY(), 4f, dotPaint);
                });

                List<FirebaseVisionPoint> leftEyeCountours = face
                        .getContour(FirebaseVisionFaceContour.LEFT_EYE).getPoints();
                leftEyeCountours.forEach(leftEyeCountour ->{
                    if(leftEyeCountours.indexOf(leftEyeCountour) !=
                            leftEyeCountours.size() - 1){
                        canvas.drawLine(leftEyeCountour.getX(), leftEyeCountour.getY(),
                                leftEyeCountours.get(leftEyeCountours.indexOf(leftEyeCountour) + 1).getX(),
                                leftEyeCountours.get(leftEyeCountours.indexOf(leftEyeCountour) + 1).getY(),
                                linePaint);
                    } else {
                        canvas.drawLine(leftEyeCountour.getX(), leftEyeCountour.getY(),
                                leftEyeCountours.get(0).getX(), leftEyeCountours.get(0).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(leftEyeCountour.getX(), leftEyeCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> rightEyeCountours = face
                        .getContour(FirebaseVisionFaceContour.RIGHT_EYE).getPoints();
                rightEyeCountours.forEach(rightEyeCountour ->{
                    if(rightEyeCountours.indexOf(rightEyeCountour) !=
                            rightEyeCountours.size() - 1){
                        canvas.drawLine(rightEyeCountour.getX(), rightEyeCountour.getY(),
                                rightEyeCountours.get(rightEyeCountours.indexOf(rightEyeCountour) + 1).getX(),
                                rightEyeCountours.get(rightEyeCountours.indexOf(rightEyeCountour) + 1).getY(),
                                linePaint);
                    } else {
                        canvas.drawLine(rightEyeCountour.getX(), rightEyeCountour.getY(),
                                rightEyeCountours.get(0).getX(), rightEyeCountours.get(0).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(rightEyeCountour.getX(), rightEyeCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> upperLipTopCountours = face
                        .getContour(FirebaseVisionFaceContour.UPPER_LIP_TOP).getPoints();
                upperLipTopCountours.forEach(upperLipTopCountour ->{
                    if(upperLipTopCountours.indexOf(upperLipTopCountour) !=
                            upperLipTopCountours.size() - 1){
                        canvas.drawLine(upperLipTopCountour.getX(), upperLipTopCountour.getY(),
                                upperLipTopCountours.get(upperLipTopCountours.indexOf(upperLipTopCountour) + 1).getX(),
                                upperLipTopCountours.get(upperLipTopCountours.indexOf(upperLipTopCountour) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(upperLipTopCountour.getX(), upperLipTopCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> upperLipBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.UPPER_LIP_BOTTOM).getPoints();
                upperLipBottomCountours.forEach(upperLipBottomCountour ->{
                    if(upperLipBottomCountours.indexOf(upperLipBottomCountour) !=
                            upperLipBottomCountours.size() - 1){
                        canvas.drawLine(upperLipBottomCountour.getX(), upperLipBottomCountour.getY(),
                                upperLipBottomCountours.get(upperLipBottomCountours.indexOf(upperLipBottomCountour) + 1).getX(),
                                upperLipBottomCountours.get(upperLipBottomCountours.indexOf(upperLipBottomCountour) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(upperLipBottomCountour.getX(), upperLipBottomCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> lowerLipTopCountours = face
                        .getContour(FirebaseVisionFaceContour.LOWER_LIP_TOP).getPoints();
                lowerLipTopCountours.forEach(lowerLipTopCountour ->{
                    if(lowerLipTopCountours.indexOf(lowerLipTopCountour) !=
                            lowerLipTopCountours.size() - 1){
                        canvas.drawLine(lowerLipTopCountour.getX(), lowerLipTopCountour.getY(),
                                lowerLipTopCountours.get(lowerLipTopCountours.indexOf(lowerLipTopCountour) + 1).getX(),
                                lowerLipTopCountours.get(lowerLipTopCountours.indexOf(lowerLipTopCountour) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(lowerLipTopCountour.getX(), lowerLipTopCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> lowerLipBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.LOWER_LIP_BOTTOM).getPoints();
                lowerLipBottomCountours.forEach(lowerLipBottomCountour ->{
                    if(lowerLipBottomCountours.indexOf(lowerLipBottomCountour) !=
                            lowerLipBottomCountours.size() - 1){
                        canvas.drawLine(lowerLipBottomCountour.getX(), lowerLipBottomCountour.getY(),
                                lowerLipBottomCountours.get(lowerLipBottomCountours.indexOf(lowerLipBottomCountour) + 1).getX(),
                                lowerLipBottomCountours.get(lowerLipBottomCountours.indexOf(lowerLipBottomCountour) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(lowerLipBottomCountour.getX(), lowerLipBottomCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> noseBottomCountours = face
                        .getContour(FirebaseVisionFaceContour.NOSE_BOTTOM).getPoints();
                noseBottomCountours.forEach(noseBottomCountour ->{
                    if(noseBottomCountours.indexOf(noseBottomCountour) !=
                            noseBottomCountours.size() - 1){
                        canvas.drawLine(noseBottomCountour.getX(), noseBottomCountour.getY(),
                                noseBottomCountours.get(noseBottomCountours.indexOf(noseBottomCountour) + 1).getX(),
                                noseBottomCountours.get(noseBottomCountours.indexOf(noseBottomCountour) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(noseBottomCountour.getX(), noseBottomCountour.getY(), 4f,
                            dotPaint);
                });

                List<FirebaseVisionPoint> noseBridgeCountours = face
                        .getContour(FirebaseVisionFaceContour.NOSE_BRIDGE).getPoints();
                noseBridgeCountours.forEach(noseBridgeCountour ->{
                    if(noseBridgeCountours.indexOf(noseBridgeCountour) !=
                            noseBridgeCountours.size() - 1){
                        canvas.drawLine(noseBridgeCountour.getX(), noseBridgeCountour.getY(),
                                noseBridgeCountours.get(noseBridgeCountours.indexOf(noseBridgeCountour) + 1).getX(),
                                noseBridgeCountours.get(noseBridgeCountours.indexOf(noseBridgeCountour) + 1).getY(),
                                linePaint);
                    }
                    canvas.drawCircle(noseBridgeCountour.getX(), noseBridgeCountour.getY(), 4f,
                            dotPaint);
                });

                if(cameraView.getFacing() == Facing.FRONT){
                    Matrix matrix = new Matrix();
                    matrix.preScale(-1f, 1f);
                    Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, Boolean.TRUE);

                    imageViewFaceDetec.setImageBitmap(flippedBitmap);
                } else {
                    imageViewFaceDetec.setImageBitmap(bitmap);
                }
            });
        }).addOnFailureListener(e -> {
            imageViewFaceDetec.setImageBitmap(null);

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
