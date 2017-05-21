package com.example.christian.aplicacionsegura.Models;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Christian on 21/05/2017.
 */

public class RealmMigration implements io.realm.RealmMigration{
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema realmSchema = realm.getSchema();
        if(oldVersion== 0){
            RealmObjectSchema   posSchema = realmSchema.create("Pos")
                        .addField("lat" , Double.class  )
                        .addField("lat" , Double.class  );
            oldVersion++;

        }
    }
}
