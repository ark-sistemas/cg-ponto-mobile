package senai.fatesg.com.cgponto.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;
import senai.fatesg.com.cgponto.interfaces.ServiceConsumer;
import senai.fatesg.com.cgponto.model.Justificativa;
import senai.fatesg.com.cgponto.model.MotivoAbono;
import senai.fatesg.com.cgponto.resources.JustificativaResource;

public class JustificativaActivity extends AppCompatActivity implements InitComponent, ServiceConsumer<Justificativa> {

    private Spinner spnCause;
    private EditText edtDescription;
    private TextView txtFileName;
    private EditText edtInitialHour;
    private EditText edtFinalHour;
    private EditText edtInitialDate;
    private EditText edtFinalDate;
    private ImageButton btnUploadFile;
    private Button btnSendCause;
    private Button btnCancelCause;
    private byte[] fileToSend;

    private Justificativa justificativa;
    private JustificativaResource justificativaResource;

    @Override
    public void initComponents() {
        spnCause = findViewById(R.id.spinner_cause);
        edtDescription = findViewById(R.id.edt_description);
        txtFileName = findViewById(R.id.txt_file_name);
        edtInitialHour = findViewById(R.id.edt_initial_hour);
        edtFinalHour = findViewById(R.id.edt_final_hour);
        edtInitialDate = findViewById(R.id.edt_initial_date);
        edtFinalDate = findViewById(R.id.edt_final_date);
        btnSendCause = findViewById(R.id.btn_send_cause);
        btnCancelCause = findViewById(R.id.btn_cancel_cause);
        btnUploadFile = findViewById(R.id.btn_upload_file);



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abono);

        // Initialize the components
        initComponents();

        //Set data in spinner
        setSpinnerData();


    }

    public void uploadFile(View view) {
        int spnIndex = spnCause.getSelectedItemPosition();

        if(isCauseWithFile(spnIndex)){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, 1212);
        } else
            Toast.makeText(this, "Não necessita de documento", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Optional<Uri> opUri = Optional.ofNullable(Objects.requireNonNull(data).getData());
        if(requestCode == 1212){
            if(resultCode == RESULT_OK){
                Uri uri = opUri.get();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                String myFilePath = myFile.getAbsolutePath();
                String displayName = null;

                try {
                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(uri,
                                    null, null, null, null);
                            if (Objects.nonNull(cursor) && cursor.moveToFirst()) {
                                displayName = cursor
                                        .getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }

                    fileToSend = Files.readAllBytes(Paths.get(uriString));
                } catch (IOException e){
                    Log.d("byte", "byte didn't read");
                }
                txtFileName.setVisibility(View.VISIBLE);
                txtFileName.setText(displayName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendCause(View view) {
        if(validateData()){
            justificativa.setTitulo(spnCause.getSelectedItem().toString());
            justificativa.setDescricao(edtDescription.getText().toString());
            justificativa.setAnexoDocumento(fileToSend);
            justificativa.setHorasDiariaInicio(edtInitialHour.getText().toString());
            justificativa.setHorasDiariaTermino(edtFinalHour.getText().toString());
            justificativa.setDataInicio(edtInitialDate.getText().toString());
            justificativa.setDataTermino(edtFinalDate.getText().toString());
            consuming(justificativa);
        }
    }

    @Override
    public void consuming(Justificativa justificativa){

        Call<Justificativa> post = justificativaResource.post(justificativa);

        post.enqueue(new Callback<Justificativa>() {
            @Override
            public void onResponse(Call<Justificativa> call, Response<Justificativa> response) {
                Toast.makeText(JustificativaActivity.this, "Justificativa enviada com sucesso",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Justificativa> call, Throwable t) {
                Toast.makeText(JustificativaActivity.this, "Justificativa não enviada.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // ---------------------- PRIVATE METHODS --------------------------

    private void setSpinnerData(){

        ArrayList<String> motivos = new ArrayList<>();
        for (MotivoAbono motivo: MotivoAbono.values()) {
            motivos.add(motivo.getDescricao());
        }
//        motivos.add("AUSÊNCIA JUSTIFICADA");
//        motivos.add("HORAS JUSTIFICADAS");
//        motivos.add("ATESTADO MÉDICO");

        ArrayAdapter<String> spnArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, motivos);

        spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCause.setAdapter(spnArrayAdapter);
    }

    private boolean isCauseWithFile(int spnIndex){
        return spnIndex == 2 || spnIndex == 3 || spnIndex == 4 || spnIndex == 5 || spnIndex == 6
                || spnIndex == 9 || spnIndex == 12;
    }

    private boolean validateData(){
        if(edtDescription.getText().toString().isEmpty()){
            Toast.makeText(this, "Coloque uma descrição para a justificativa",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if(edtInitialDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Coloque uma data de início para a justificativa",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtFinalDate.getText().toString().isEmpty()){
            Toast.makeText(this, "Coloque uma data de fim para a justificativa",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if(edtInitialHour.getText().toString().isEmpty()){
            Toast.makeText(this, "Coloque uma hora de início para a justificativa",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if(edtFinalHour.getText().toString().isEmpty()){
            Toast.makeText(this, "Coloque uma hora de início para a justificativa",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
