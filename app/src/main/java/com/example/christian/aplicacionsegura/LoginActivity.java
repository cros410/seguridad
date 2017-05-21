package com.example.christian.aplicacionsegura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.aplicacionsegura.Models.RealmMigration;
import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.Response.LoginResponse;
import com.example.christian.aplicacionsegura.Retrofit.Connection;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView  txv_registrarse;
    TextView txv_olvidar;
    Button btn_login;
    ProgressBar progressBar ;
    EditText edt_correo , edt_password;
    Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_correo = (EditText) findViewById(R.id.edt_correo);
        edt_password = (EditText) findViewById(R.id.edt_password);
        txv_registrarse = (TextView) findViewById(R.id.txv_registrarse);
        txv_olvidar = (TextView) findViewById(R.id.txv_olvidar);
        btn_login = (Button)  findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.prg_login);
        progressBar.setVisibility(View.INVISIBLE);

        txv_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , RegistroActivity.class);
                startActivity(intent);
            }
        });

        txv_olvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , OlvidarActivity.class);
                startActivity(intent);
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c = edt_correo.getText().toString();
                String p = edt_password.getText().toString();
                if(c.equalsIgnoreCase("") || p.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this,"Completar datos" , Toast.LENGTH_SHORT).show();
                }else{
                    user = new Usuario();
                    user.setCorreo(c);
                    user.setPassword(p);
                    Call<LoginResponse> getLogin = Connection.getService().getDataFotLogin(user);
                    btn_login.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    getLogin.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            LoginResponse loginResponse = response.body();
                            if(loginResponse.getCod() != 1){
                                Toast.makeText(LoginActivity.this
                                        ,loginResponse.getMsg()
                                        ,Toast.LENGTH_SHORT).show();
                                btn_login.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                            }else{
                                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                                //REALM
                                configRealm();
                                Realm.init(LoginActivity.this);
                                Realm realm = Realm.getDefaultInstance();
                                RealmHelper realmHelper = new RealmHelper(realm);
                                realmHelper.deleteUsers();
                                Usuario u = loginResponse.getData().getUsuario();
                                realmHelper.saveUsuario(u);
                                //
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(LoginActivity.this
                                    ,"Intentar despues"
                                    ,Toast.LENGTH_SHORT).show();
                            btn_login.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }
        });

    }

    public void  configRealm(){
        Realm.init(getApplicationContext());
        // create your Realm configuration
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);
    }

}
