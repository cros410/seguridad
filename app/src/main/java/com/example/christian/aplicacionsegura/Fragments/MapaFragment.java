package com.example.christian.aplicacionsegura.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.christian.aplicacionsegura.MainActivity;
import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.R;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.RegistrarIncidenciaActivity;
import com.example.christian.aplicacionsegura.Response.ResponseIncidencias;
import com.example.christian.aplicacionsegura.Retrofit.Connection;
import com.example.christian.aplicacionsegura.Suport.IncidenciaAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;


public class MapaFragment extends Fragment  implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    FloatingActionButton flt_registrar_incidencia;
    Context context = getContext();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);
        flt_registrar_incidencia = (FloatingActionButton) view.findViewById(R.id.flt_registrar_incidencia);
        flt_registrar_incidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , RegistrarIncidenciaActivity.class);
                startActivity(intent);
            }
        });

        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            mMap.setMyLocationEnabled(true);
        }

        //
        Call<ResponseIncidencias> saveTok = Connection.getService().getAll();
        saveTok.enqueue(new Callback<ResponseIncidencias>() {
            @Override
            public void onResponse(Call<ResponseIncidencias> call, retrofit2.Response<ResponseIncidencias> response) {
                ResponseIncidencias res = response.body();
                List<Incidencia> ins = new ArrayList<Incidencia>();
                Incidencia in;
                //REALM
                Realm.init(getContext());
                Realm realm = Realm.getDefaultInstance();
                RealmHelper realmHelper = new RealmHelper(realm);
                realmHelper.deleteIncidencias();
                MarkerOptions markerOptions;
                for(Incidencia d : res.getData()){
                   markerOptions = new MarkerOptions()
                           .position(new LatLng(d.getPos().getLat() , d.getPos().getLon()))
                           .icon(BitmapDescriptorFactory.fromResource(getIconMarker(d.getTipo())))
                   .title(d.getTitulo());
                    mMap.addMarker(markerOptions);
                }
                enableMyLocation();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.0477466,-77.0477984), 12.0f));
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        Toast.makeText(getContext(), "Ajustando...", Toast.LENGTH_SHORT).show();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.0477466,-77.0477984), 12.0f));
                       return true;
                    }
                });


            }
            @Override
            public void onFailure(Call<ResponseIncidencias> call, Throwable t) {
                Log.d("CASI:" ,"FALLOOOOOOOOOOOO");
            }
        });

        //

        /*MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(-12.0477466,-77.0477000)).title("Comentar");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.crime_arma));
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.0477466,-77.0477984), 12.0f));
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();*/
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((MainActivity)getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    public int getIconMarker(String tipo){
        switch (tipo){
            case "VIOLENCIA":
                return R.drawable.crime_violencia;

            case "ARMA":
                return R.drawable.crime_arma;

            case "PANDILLAJE":
                return R.drawable.crime_pandillaje;

            default:
                return  R.drawable.crime_robo;

        }
    }

}
