package com.example.christian.aplicacionsegura.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.R;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.Response.ResponseIncidencias;
import com.example.christian.aplicacionsegura.Retrofit.Connection;
import com.example.christian.aplicacionsegura.Suport.IncidenciaAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;

public class MuroFragment extends Fragment {

    public RecyclerView recyclerView;
    public LinearLayoutManager linearLayout;
    private ProgressBar prg_listar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_muro, container, false);
        setupRecyclerView(v);
        setupWindowAnimations();
        return v;
    }

    private void setupRecyclerView(View v) {

        linearLayout = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.reciclador);
        recyclerView.setLayoutManager(linearLayout);
        prg_listar  = (ProgressBar) v.findViewById(R.id.prg_listar);

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

                for(Incidencia d : res.getData()){
                    in = new Incidencia();
                    realmHelper.saveIncidencia(d);
                    in.setTitulo(d.getTitulo());
                    in.setDescripcion(d.getDescripcion());
                    in.setDistrito(d.getDistrito());
                    in.setFoto(d.getFoto());
                    in.setIdUsuario(d.getIdUsuario());
                    in.setId(d.getId());
                    ins.add(in);
                }
                //

                IncidenciaAdapter adapter = new IncidenciaAdapter(getContext(), ins);
                recyclerView.setAdapter(adapter);
                prg_listar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ResponseIncidencias> call, Throwable t) {
                Log.d("CASI:" ,"FALLOOOOOOOOOOOO");
            }
        });


    }

    private void setupWindowAnimations() {
        getActivity().getWindow().setReenterTransition(new Explode());
        getActivity().getWindow().setExitTransition(new Explode().setDuration(500));
    }

}
