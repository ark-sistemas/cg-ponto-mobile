package senai.fatesg.com.cgponto.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class CameraActivity extends AppCompatActivity implements InitComponent {

    protected ImageView picture;
    protected Button btnTakePicture;
    private static final String TAG = "CapturePicture";
    static final int REQUEST_PICTURE_CAPTURE = 1;
    protected String pictureFilePath;
    protected String deviceIdentifier;
    protected AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initComponents();
        getInstallationIdentifier();
        setListeners();
    }

    @Override
    public void initComponents() {
        picture = findViewById(R.id.picture);
        btnTakePicture = findViewById(R.id.btn_take_pic);
        dialog = buildDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists())            {
                picture.setImageURI(Uri.fromFile(imgFile));
//                dialog.show();
            }
        }
    }

    private void setListeners(){
        btnTakePicture.setOnClickListener(v -> {
            RelativeLayout.LayoutParams params = new RelativeLayout
                    .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 0);
            picture.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            picture.setLayoutParams(params);
            sendTakePictureIntent();

        });
    }

    private void sendTakePictureIntent() {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

                File pictureFile;
                try {
                    pictureFile = getPictureFile();
                } catch (IOException ex) {
                    Toast.makeText(this,
                            getString(R.string.hint_camera_picture),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pictureFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.cgponto.android.fileprovider",
                            pictureFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
                }
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ROOT).format(new Date());
        String pictureFile = "FACE_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.getExternalStorageState());
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    private AlertDialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.label_dialog_title));
        builder.setMessage(getString(R.string.label_dialog_message));

        builder.setPositiveButton(R.string.label_dialog_yes, (dialog, which) -> {
            Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(pictureFilePath);
            Uri picUri = Uri.fromFile(f);
            galleryIntent.setData(picUri);
            this.sendBroadcast(galleryIntent);
        });

        builder.setNegativeButton(R.string.label_dialog_no, (dialog, which) -> {
        });


        return builder.create();
    }

    protected synchronized String getInstallationIdentifier() {
        if (deviceIdentifier == null) {
            SharedPreferences sharedPrefs = this.getSharedPreferences(
                    "DEVICE_ID", Context.MODE_PRIVATE);
            deviceIdentifier = sharedPrefs.getString("DEVICE_ID", null);
            if (deviceIdentifier == null) {
                deviceIdentifier = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("DEVICE_ID", deviceIdentifier);
                editor.commit();
            }
        }
        return deviceIdentifier;
    }


}