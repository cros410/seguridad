package com.example.christian.aplicacionsegura.Models;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Christian on 19/05/2017.
 */

public class Migration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0) {
            RealmObjectSchema usuarioSchema = schema.get("Usuario");
            usuarioSchema.addPrimaryKey("id");
            oldVersion++;
        }

    }

}
