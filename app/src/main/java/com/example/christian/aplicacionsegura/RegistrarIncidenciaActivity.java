package com.example.christian.aplicacionsegura;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrarIncidenciaActivity extends AppCompatActivity {

    private EditText edt_ubicacion;
    int PLACE_PICKER_REQUEST =1;
    int SELECT_FILE = 0 , REQUEST_PERMITION_STORAGE = 1;
    Toolbar toolbar ;
    private GestureDetectorCompat gesture;
    Map config;
    private String pathClaudinary;
    Handler handler = new Handler();
    ViewPager viewPager;
    CustomSwiteAdapter customSwiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_incidencia);
        //toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //toolbar.setTitle("Registrar Insidencia");

        /*edt_ubicacion = (EditText) findViewById(R.id.edt_ing_ubicacion);
        edt_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder =
                        new PlacePicker.IntentBuilder();

                try {

                    startActivityForResult(builder.build(RegistrarIncidenciaActivity.this) ,PLACE_PICKER_REQUEST );
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });*/

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        customSwiteAdapter = new CustomSwiteAdapter(this);
        viewPager.setAdapter(customSwiteAdapter);
        gesture = new  GestureDetectorCompat(this , new LearnGesture());


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gesture.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    protected void onActivityResult(int requestCode , int resultCode , Intent data){

       /* if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode== RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                String adress = String.format("Lat: %s" , place.getAddress());
                edt_ubicacion.setText(adress);
            }
        }*/
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                pathClaudinary = getPath(RegistrarIncidenciaActivity.this,selectedImageUri);
                new Thread(){
                    @Override
                    public void run() {
                        subirCloudinary();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrarIncidenciaActivity.this, "Foto Subida", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();
                //Toast.makeText(RegistrarIncidenciaActivity.this , path ,Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void subirCloudinary() {
        config = new HashMap();
        config.put("cloud_name", "jamacomida");
        config.put("api_key", "762451111153581");
        config.put("api_secret", "NtkHjwI3NRVmLtWpOrOK2LA-Kx0");
        Cloudinary cloudinary = new Cloudinary(config);
        File photoFile = new File(pathClaudinary);
        try {
            FileInputStream fileInputStream = new FileInputStream(photoFile);
            String timestamp = String.valueOf(new Date().getTime());
            cloudinary.uploader().upload(fileInputStream,
                    ObjectUtils.asMap("public_id", timestamp)
            );
            Log.i("URL_FOTO", cloudinary.url().generate(timestamp));
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e2.getX() > e1.getX()){
                Toast.makeText(RegistrarIncidenciaActivity.this , "Izquierda a derecha" ,Toast.LENGTH_SHORT).show();
            }else if(e2.getX() < e1.getX()){

                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMITION_STORAGE);

            }
            return true;

        }
    }

    public void subirFoto(){
        Toast.makeText(RegistrarIncidenciaActivity.this , "Derecha a ezquierda" ,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent.createChooser(intent , "Seleccionar Archivo"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        if(requestCode== REQUEST_PERMITION_STORAGE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
               subirFoto();
            }else{
                Toast.makeText(this,"Se necesita el permiso para guardar imagenes",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) S|election arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
