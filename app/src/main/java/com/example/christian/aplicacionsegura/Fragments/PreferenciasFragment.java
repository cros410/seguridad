package com.example.christian.aplicacionsegura.Fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.R;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.Response.LoginResponse;
import com.example.christian.aplicacionsegura.Retrofit.Connection;
import com.example.christian.aplicacionsegura.Suport.GPS_Service;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PreferenciasFragment extends Fragment {

    private SeekBar seekBar ;
    private TextView txv_rango;
    private   int progreso_rango;
    private ImageButton imb_actualizar_rango;
    private ProgressBar pgb_up_rango;
    private Button btn_start , btn_stop;
    private BroadcastReceiver broadcastReceiver;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_preferencias, container, false);

        btn_start = (Button) v.findViewById(R.id.btn_service_start);
        btn_stop = (Button) v.findViewById(R.id.btn_service_stop);

        seekBar = (SeekBar) v.findViewById(R.id.seb_rango);
        txv_rango = (TextView) v.findViewById(R.id.txv_rango);
        imb_actualizar_rango = (ImageButton) v.findViewById(R.id.btn_actualizar_rango);
        pgb_up_rango = (ProgressBar) v.findViewById(R.id.pgb_up_rango);
        pgb_up_rango.setVisibility(View.INVISIBLE);

        imb_actualizar_rango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext() , "Actualizando rango : " + txv_rango.getText() , Toast.LENGTH_SHORT);
                //REALM
                Realm.init(getContext());
                Realm realm = Realm.getDefaultInstance();
                RealmHelper realmHelper = new RealmHelper(realm);
                Usuario u = realmHelper.findUser();
                final Usuario usuario = new Usuario();
                int r = Integer.parseInt(txv_rango.getText().toString());
                usuario.setRango(r);
                usuario.setCorreo(u.getCorreo());
                Call<LoginResponse> updateRango = Connection.getService().updateRangoUsuario(usuario);
                pgb_up_rango.setVisibility(View.VISIBLE);
                updateRango.enqueue(new Callback<LoginResponse>() {
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
                            realmHelper.updateRangoUser(usuario);

                        }
                        pgb_up_rango.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(getContext()
                                ,"Intentar despues"
                                ,Toast.LENGTH_SHORT).show();
                        pgb_up_rango.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });

        seekBar.setMax(500);

        //REALM
        Realm.init(getContext());
        Realm realm = Realm.getDefaultInstance();
        RealmHelper realmHelper = new RealmHelper(realm);
        Usuario u = realmHelper.findUser();
        //
        seekBar.setProgress(u.getRango());
        txv_rango.setText(u.getRango()+ "");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progreso_rango = progress;
                txv_rango.setText(progreso_rango+ "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if(!runtime_permissions())
            enable_buttons();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String lat = intent.getExtras().get("coordinates_lat").toString();
                    String lon = intent.getExtras().get("coordinates_lon").toString();
                    double f_lat = Double.parseDouble(lat);
                    double f_lon = Double.parseDouble(lon);



                    //Toast.makeText(getContext(), "Lat : " + f_lat, Toast.LENGTH_SHORT).show();
                    //textView.append("\n" +intent.getExtras().get("coordinates"));

                }
            };
        }
        getContext().registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            getContext().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }
    }

    private void enable_buttons() {

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),GPS_Service.class);
                getContext().startService(i);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(),GPS_Service.class);
                getContext().stopService(i);

            }
        });

    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }




}
