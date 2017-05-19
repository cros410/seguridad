package com.example.christian.aplicacionsegura.Realm;

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
    public ArrayList<Usuario>  findUser(){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        RealmResults<Usuario>  r_users = realm.where(Usuario.class).findAll();
        for (Usuario u : r_users ){
            usuarios.add(u);
        }
        return usuarios;
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


}
