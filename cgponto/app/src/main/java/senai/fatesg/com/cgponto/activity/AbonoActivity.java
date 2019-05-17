package senai.fatesg.com.cgponto.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import senai.fatesg.com.cgponto.R;
import senai.fatesg.com.cgponto.interfaces.InitComponent;

public class AbonoActivity extends AppCompatActivity implements InitComponent {

    Spinner spnCause;
    EditText edtDescription;
    TextView txtFileName;
    EditText edtInitialHour;
    EditText edtFinalHour;
    EditText edtInitialDate;
    EditText edtFinalDate;
    ImageButton btnUploadFile;
    Button btnSendCause;
    Button btnCancelCause;

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
            Toast.makeText(this, "Envie documento", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Não necessita de documento", Toast.LENGTH_SHORT).show();

    }


    // ---------------------- PRIVATE METHODS --------------------------
    private void setSpinnerData(){
        ArrayList<String> motivos = new ArrayList<>();
        motivos.add("AUSÊNCIA JUSTIFICADA");
        motivos.add("HORAS JUSTIFICADAS");
        motivos.add("ATESTADO MÉDICO");

        ArrayAdapter<String> spnArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, motivos);

        spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCause.setAdapter(spnArrayAdapter);
    }

    private boolean isCauseWithFile(int spnIndex){
        return spnIndex == 2;
    }
}
