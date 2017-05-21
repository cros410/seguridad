package com.example.christian.aplicacionsegura.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.christian.aplicacionsegura.LoginActivity;
import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.R;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.Response.LoginResponse;
import com.example.christian.aplicacionsegura.Retrofit.Connection;
import com.example.christian.aplicacionsegura.Suport.CircleTransform;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {

    private EditText edt_correo , edt_nombre , edt_celular , edt_distrito;
    private ImageView imv_foto;
    private TextInputLayout til_correo;
    private boolean edit;
    private FloatingActionButton flb_edit;
    private Button btn_actualizar;
    private ProgressBar pgr_actualizar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        edit = false;
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        btn_actualizar = (Button) v.findViewById(R.id.btn_actualizar);
        btn_actualizar.setVisibility(View.INVISIBLE);
        edt_correo = (EditText) v.findViewById(R.id.edt_correo);
        edt_correo.setEnabled(false);
        edt_nombre = (EditText) v.findViewById(R.id.edt_nombre);
        edt_celular = (EditText) v.findViewById(R.id.edt_celular);
        edt_distrito = (EditText) v.findViewById(R.id.edt_ubicacion);
        imv_foto = (ImageView) v.findViewById(R.id.imv_foto);
        flb_edit = (FloatingActionButton) v.findViewById(R.id.flb_edit);
        pgr_actualizar = (ProgressBar) v.findViewById(R.id.pgb_actualizar);
        pgr_actualizar.setVisibility(View.INVISIBLE);


        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  correo , nombre , distrito , numero;
                correo = edt_correo.getText().toString();
                nombre = edt_nombre.getText().toString();
                distrito = edt_distrito.getText().toString();
                numero = edt_celular.getText().toString();

                if(correo.equalsIgnoreCase("") || nombre.equalsIgnoreCase("")
                        || distrito.equalsIgnoreCase("") || numero.equalsIgnoreCase("")){
                    Toast.makeText(getContext(),"Completar datos" , Toast.LENGTH_SHORT);
                    btn_actualizar.setVisibility(View.VISIBLE);
                }else{
                    final Usuario us = new Usuario();
                    us.setDistrito(distrito);
                    us.setCorreo(correo);
                    us.setNumero(numero);
                    us.setNombre(nombre);
                    //RETROFIT
                    Call<LoginResponse> updatePerfil = Connection.getService().updateUserPerfil(us);
                    btn_actualizar.setVisibility(View.INVISIBLE);
                    pgr_actualizar.setVisibility(View.VISIBLE);
                    updatePerfil.enqueue(new Callback<LoginResponse>() {
                         @Override
                         public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                             LoginResponse loginResponse = response.body();
                             if(loginResponse.getCod() != 1){
                                 Toast.makeText(getContext()
                                         ,loginResponse.getMsg()
                                         ,Toast.LENGTH_SHORT).show();
                             }else{
                                 // REALM
                                 Realm.init(getContext());
                                 Realm realm = Realm.getDefaultInstance();
                                 RealmHelper realmHelper = new RealmHelper(realm);
                                 realmHelper.updateUser(us);
                                 mostrarUsuario();
                             }
                             pgr_actualizar.setVisibility(View.INVISIBLE);
                         }
                        @Override
                         public void onFailure(Call<LoginResponse> call, Throwable t) {
                            Toast.makeText(getContext()
                                    ,"Intentar despues"
                                    ,Toast.LENGTH_SHORT).show();
                            pgr_actualizar.setVisibility(View.INVISIBLE);
                         }
                    });
                }


            }
        });

        flb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit){
                    changeEstadoEditText(false);
                }else{
                    changeEstadoEditText(true);
                }
            }
        });
        changeEstadoEditText(false);
        mostrarUsuario();
        return v;
    }

    public  void changeEstadoEditText(boolean estado){

        if(estado){
            edt_celular.setEnabled(true);
            edt_nombre.setEnabled(true);
            edt_distrito.setEnabled(true);
            btn_actualizar.setVisibility(View.VISIBLE);
            edit = true;
        }else{
            edt_celular.setEnabled(false);
            edt_nombre.setEnabled(false);
            edt_distrito.setEnabled(false);
            btn_actualizar.setVisibility(View.INVISIBLE);
            edit = false;
        }
    }

    public void mostrarUsuario(){
        //REALM
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmHelper realmHelper = new RealmHelper(realm);
        Usuario u = realmHelper.findUser();
        edt_correo.setText(u.getCorreo());
        edt_nombre.setText(u.getNombre());
        edt_distrito.setText(u.getDistrito());
        edt_celular.setText(u.getNumero());
        Picasso.with(getContext()).load(u.getFoto()).transform(new CircleTransform()).into(imv_foto);
        changeEstadoEditText(false);
        //
    }

}
