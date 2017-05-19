package com.example.christian.aplicacionsegura;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.aplicacionsegura.Models.Notificacion;
import com.example.christian.aplicacionsegura.Models.TestBody;
import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.Retrofit.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    Button button;
    TextView textView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        final RealmConfiguration config = new RealmConfiguration.Builder()
                .assetFile("default.realm") // e.g "default.realm" or "lib/data.realm"
                .deleteRealmIfMigrationNeeded()
                .build();

        button = (Button) findViewById(R.id.test_boton);
        textView = (TextView) findViewById(R.id.test_texto);
        progressBar = (ProgressBar) findViewById(R.id.p_esperar) ;
        progressBar.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Si funciona");
                TestBody testBody = new TestBody();
                testBody.setCorreo("christian@gmail.com");
                Call<Usuario> getUsuario = Connection.getService().getUserByCorreo(testBody);
                progressBar.setVisibility(View.VISIBLE);
                getUsuario.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                        Usuario usuario = response.body();
                        // REALM TRANSACTION

                        Realm.init(TestActivity.this);
                        Realm realm = Realm.getInstance(config);

                        RealmHelper realmHelper = new RealmHelper(realm);
                        //realmHelper.deleteUsers();
                        realmHelper.saveUsuario(usuario);
                        ArrayList<Usuario> users = realmHelper.findUser();
                        String texto = "";
                        for(Usuario u : users){
                           texto = texto +  u.getCorreo() + " \n";
                        }
                        textView.setText(texto);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(TestActivity.this,"Error",Toast.LENGTH_LONG);
                    }
                });
            }
        });
    }


    private String copyBundledRealmFile(InputStream inputStream, String outFileName) {
        try {
            File file = new File(this.getFilesDir(), outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
