package com.example.christian.aplicacionsegura;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.christian.aplicacionsegura.Models.TestBody;
import com.example.christian.aplicacionsegura.Retrofit.Connection;

public class TestActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        button = (Button) findViewById(R.id.test_boton);
        textView = (TextView) findViewById(R.id.test_texto);
        progressBar = (ProgressBar) findViewById(R.id.p_esperar) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Si funciona");
                //progressBar.setVisibility(View.GONE);
                TestBody testBody = new TestBody();
                testBody.setCorreo("christian@gmail.com");
                Connection.getService().getUserByCorreo(testBody);

            }
        });

    }
}
