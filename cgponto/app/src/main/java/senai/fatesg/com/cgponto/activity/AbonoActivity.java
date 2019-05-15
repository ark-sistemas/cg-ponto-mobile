package senai.fatesg.com.cgponto.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import senai.fatesg.com.cgponto.R;

public class AbonoActivity extends AppCompatActivity {

    Spinner spnMotivos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abono);

        spnMotivos = findViewById(R.id.spinner_motivos);


        ArrayList<String> motivos = new ArrayList<>();
        motivos.add("AUSÊNCIA JUSTIFICADA");
        motivos.add("HORAS JUSTIFICADAS");
        motivos.add("ATESTADO MÉDICO");

        ArrayAdapter<String> spnArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, motivos);
        spnArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMotivos.setAdapter(spnArrayAdapter);
    }
}
