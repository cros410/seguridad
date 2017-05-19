package com.example.christian.aplicacionsegura;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Christian on 19/05/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
