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

            //Face
            Paint facePaint = new Paint();
            facePaint.setColor(Color.RED);
            facePaint.setStyle(Paint.Style.STROKE);
            facePaint.setStrokeWidth(4f);

            firebaseVisionFaces.forEach(face ->{

                canvas.drawRect(face.getBoundingBox(), facePaint);


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
