package com.example.christian.aplicacionsegura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PresentationActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);

        btnIngresar = (Button) findViewById(R.id.btn_ingresar);
        btnIngresar.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this , LoginActivity.class);
        startActivity(intent);
    }
}
