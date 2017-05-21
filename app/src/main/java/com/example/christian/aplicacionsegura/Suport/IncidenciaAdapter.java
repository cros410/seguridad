package com.example.christian.aplicacionsegura.Suport;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.christian.aplicacionsegura.DetalleActivity;
import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.IncidenciaViewHolder>
        implements ItemClickListener {
        private final Context context;
        private List<Incidencia> items;


    public IncidenciaAdapter(Context context, List<Incidencia> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public IncidenciaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);
        return new IncidenciaViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(IncidenciaViewHolder viewHolder, int i) {
        // Item procesado actualmente
        final Incidencia currentItem = items.get(i);
        viewHolder.titulo.setText(currentItem.getTitulo());
        viewHolder.usuario.setText(currentItem.getIdUsuario());
        viewHolder.distrito.setText(currentItem.getDistrito());
        viewHolder.descripcion.setText(currentItem.getDescripcion());

        viewHolder.imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Imagen a compartir entre transiciones
                View sharedImage = view.findViewById(R.id.imv_imagen);

                DetalleActivity.launch(
                        (Activity) context, currentItem.getId(), sharedImage);
            }
        });

        Picasso.with(context).load(currentItem.getFoto()).into(viewHolder.imagen);

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    /**
     * View holder para reciclar elementos
     */
    public static class IncidenciaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Views para un curso
        public final TextView titulo;
        public final TextView usuario;
        public final TextView distrito;
        public final TextView descripcion;
        public final ImageView imagen;


        // Interfaz de comunicación
        public ItemClickListener listener;

        public IncidenciaViewHolder(View v, ItemClickListener listener) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.txv_titulo);
            usuario = (TextView) v.findViewById(R.id.txv_nombre);
            distrito= (TextView) v.findViewById(R.id.txv_distrito);
            descripcion = (TextView) v.findViewById(R.id.txv_descripcion);
            imagen = (ImageView) v.findViewById(R.id.imv_imagen);



            v.setOnClickListener(this);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}

interface ItemClickListener {
    void onItemClick(View view, int position);
}