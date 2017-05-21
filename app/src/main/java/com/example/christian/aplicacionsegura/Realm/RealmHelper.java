package com.example.christian.aplicacionsegura.Realm;

import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.Models.Usuario;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Christian on 18/05/2017.
 */

public class RealmHelper {

    Realm realm;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }

    //WRITE USER
    public void saveUsuario(final Usuario usuario){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Usuario u = realm.copyToRealm(usuario);
            }
        });

    }

    //READ USER
    public Usuario  findUser(){
        Usuario usuario ;
        usuario = realm.where(Usuario.class).findFirst();
        return usuario;
    }

    //DELETE ALL USERS

    public void deleteUsers(){

        final RealmResults<Usuario> results = realm.where(Usuario.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    //UPDATE USER

    public void updateUser(Usuario user){
        realm.beginTransaction();
        Usuario u = realm.where(Usuario.class).findFirst();
        u.setNombre(user.getNombre());
        u.setNumero(user.getNumero());
        u.setDistrito(user.getDistrito());
        realm.commitTransaction();
    }

    //UPDATE RANGO USER
    public void updateRangoUser(Usuario user){
        realm.beginTransaction();
        Usuario u = realm.where(Usuario.class).findFirst();
        u.setRango(user.getRango());
        realm.commitTransaction();
    }

    //WRITE incidencia
    public void saveIncidencia(final Incidencia datum){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Incidencia u = realm.copyToRealm(datum);
            }
        });

    }

    //READ Incidencia
    public Incidencia findIncidencia(String id ){
        Incidencia datum = new Incidencia();
        datum = realm.where(Incidencia.class).equalTo("id", id).findFirst();
        return datum;
    }

    //DELETE ALL Incidencias

    public void deleteIncidencias(){

        final RealmResults<Incidencia> results = realm.where(Incidencia.class).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

}
