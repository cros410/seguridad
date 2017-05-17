package com.example.christian.aplicacionsegura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class RegistrarIncidenciaActivity extends AppCompatActivity {

    private EditText edt_ubicacion;
    int PLACE_PICKER_REQUEST =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_incidencia);

        /*spinner = (Spinner) findViewById(R.id.spn_tipo);
        ArrayAdapter<CharSequence> charSequenceArrayAdapter = ArrayAdapter.createFromResource(this,R.array.tipos,
            android.R.layout.simple_spinner_item);
         charSequenceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(charSequenceArrayAdapter);*/

        edt_ubicacion = (EditText) findViewById(R.id.edt_ing_ubicacion);
        edt_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder =
                        new PlacePicker.IntentBuilder();

                try {

                    startActivityForResult(builder.build(RegistrarIncidenciaActivity.this) ,PLACE_PICKER_REQUEST );
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    protected void onActivityResult(int requestCode , int resultCode , Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode== RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                String adress = String.format("Lat: %s" , place.getAddress());
                edt_ubicacion.setText(adress);
            }
        }

    }
}
