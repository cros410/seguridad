package com.example.christian.aplicacionsegura;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class DetalleActivity extends AppCompatActivity {

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Bundle bundle =  getIntent().getExtras();

        String position = bundle.getString("ID_INCIDENCIA");

        setupViews(position);

        Window window = getWindow();
        Transition t3 = TransitionInflater.from(this)
               .inflateTransition(R.transition.detail_enter_trasition);
        window.setEnterTransition(t3);
    }

    private void setupViews(String position) {
        TextView titulo = (TextView) findViewById(R.id.detail_titulo);
        TextView description = (TextView) findViewById(R.id.detail_description);
        TextView usuario = (TextView) findViewById(R.id.detail_usuario);
        TextView tipo = (TextView) findViewById(R.id.detail_tipo);
        ImageView imagen = (ImageView) findViewById(R.id.detail_imagen);

        //REALM
        Realm.init(DetalleActivity.this);
        Realm realm = Realm.getDefaultInstance();
        RealmHelper realmHelper = new RealmHelper(realm);
        Incidencia d = realmHelper.findIncidencia(position);
        //
        Picasso.with(context).load(d.getFoto()).into(imagen);
        titulo.setText(d.getTitulo());
        description.setText(d.getDescripcion());
        usuario.setText("Creado Por:" + d.getIdUsuario());
        tipo.setText(d.getTipo());


    }

    public static void launch(Activity context, String position, View sharedView) {
        Intent intent = new Intent(context, DetalleActivity.class);

        intent.putExtra("ID_INCIDENCIA", position);

        ActivityOptions options0 = ActivityOptions
                .makeSceneTransitionAnimation(context, sharedView, sharedView.getTransitionName());
        context.startActivity(intent, options0.toBundle());


    }
}
