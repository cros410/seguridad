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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.christian.aplicacionsegura.Models.Incidencia;
import com.example.christian.aplicacionsegura.Models.Pos;
import com.example.christian.aplicacionsegura.Models.Usuario;
import com.example.christian.aplicacionsegura.Realm.RealmHelper;
import com.example.christian.aplicacionsegura.Response.LoginResponse;
import com.example.christian.aplicacionsegura.Retrofit.Connection;
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

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarIncidenciaActivity extends AppCompatActivity {

    private EditText edt_ubicacion;
    int PLACE_PICKER_REQUEST =1;
    int SELECT_FILE = 0 , REQUEST_PERMITION_STORAGE = 1;
    private String img_link;
    private GestureDetectorCompat gesture;
    Map config;
    private String pathClaudinary;
    Handler handler = new Handler();
    ViewPager viewPager;
    CustomSwiteAdapter customSwiteAdapter;
    private TextView txv_tipo;
    private Button btn_add_incidencia;
    private ViewPager view_pager;
    private ImageView imv_add_foto;
    private ProgressBar pgb_cargar;
    private boolean isImage = false;
    private final String link = "http://res.cloudinary.com/jamacomida/image/upload/v1495398095/";
    private EditText edt_titulo, edt_descripcion;
    private String ti, des , dis;
    private double lon , lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_incidencia);
        txv_tipo = (TextView) findViewById(R.id.txv_tipo);
        edt_ubicacion = (EditText) findViewById(R.id.edt_distrito);
        btn_add_incidencia =(Button) findViewById(R.id.btn_add_incidencia);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        imv_add_foto = (ImageView) findViewById(R.id.imv_add_foto);
        pgb_cargar = (ProgressBar) findViewById(R.id.pgb_cargar);
        edt_titulo = (EditText) findViewById(R.id.edt_titulo);
        edt_descripcion = (EditText) findViewById(R.id.edt_descripcion);

        pgb_cargar.setVisibility(View.INVISIBLE);


        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(RegistrarIncidenciaActivity.this , "position : " + position , Toast.LENGTH_SHORT).show();
                if(position == 0){
                    txv_tipo.setText("ARMA");
                }else if(position == 1){
                    txv_tipo.setText("PANDILLAJE");
                }else if(position == 2){
                    txv_tipo.setText("VIOLENCIA");
                }else if(position == 3){
                    txv_tipo.setText("ROBO");
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



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
        });

        btn_add_incidencia.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                ti =  edt_titulo.getText().toString();
                des = edt_descripcion.getText().toString();
                dis = edt_ubicacion.getText().toString();

               if( ti.equalsIgnoreCase("")
                       || des.equals("")
                            || dis.equals("")){
                   Toast.makeText(RegistrarIncidenciaActivity.this , "Completar campos", Toast.LENGTH_SHORT).show();

               }else{
                    // REALM GET USER
                   Realm.init(RegistrarIncidenciaActivity.this);
                   Realm realm = Realm.getDefaultInstance();
                   RealmHelper realmHelper = new RealmHelper(realm);
                   Usuario u = realmHelper.findUser();
                   //
                   Incidencia i = new Incidencia();
                   Pos pos = new Pos();
                   pos.setLat(lat);
                   pos.setLon(lon);
                   i.setIdUsuario(u.getNombre());
                   i.setTitulo(ti);
                   i.setFoto(img_link);
                   i.setTipo(txv_tipo.getText().toString());
                   i.setDistrito(dis);
                   i.setDescripcion(des);
                   i.setPos(pos);
                   setVisible(true);
                   Call<LoginResponse> saveIncidencia = Connection.getService().saveIncidencia(i);
                   saveIncidencia.enqueue(new Callback<LoginResponse>() {
                       @Override
                       public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                           LoginResponse loginResponse = response.body();
                           if(loginResponse.getCod() !=1 ){
                               Toast.makeText(RegistrarIncidenciaActivity.this,
                                        "Intentar de nuevo" , Toast.LENGTH_SHORT).show();
                           }else{
                               Toast.makeText(RegistrarIncidenciaActivity.this,
                                       loginResponse.getMsg() , Toast.LENGTH_SHORT).show();
                           }
                           setVisible(false);
                       }

                       @Override
                       public void onFailure(Call<LoginResponse> call, Throwable t) {
                           Toast.makeText(RegistrarIncidenciaActivity.this,
                                   "ERROR INTENTAR DE NUEVO" , Toast.LENGTH_SHORT).show();
                           setVisible(false);
                       }
                   });

               }

            }
        });

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

        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode== RESULT_OK){
                Place place = PlacePicker.getPlace(data,this);
                String adress = String.format("%s" , place.getAddress());
                lon = place.getLatLng().longitude;
                lat = place.getLatLng().latitude;
                edt_ubicacion.setText(adress);
            }
        }
        if(resultCode == Activity.RESULT_OK){
            if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                pathClaudinary = getPath(RegistrarIncidenciaActivity.this,selectedImageUri);
                imv_add_foto.setVisibility(View.INVISIBLE);
                pgb_cargar.setVisibility(View.VISIBLE);
                new Thread(){
                    @Override
                    public void run() {

                        subirCloudinary();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistrarIncidenciaActivity.this, "Foto cargada", Toast.LENGTH_SHORT).show();
                                setVisible(false);
                            }
                        });
                    }
                }.start();

            }
        }

    }

    public void setVisible(boolean es){
        if(es){
            imv_add_foto.setVisibility(View.INVISIBLE);
            pgb_cargar.setVisibility(View.VISIBLE);
        }else{
            imv_add_foto.setVisibility(View.VISIBLE);
            pgb_cargar.setVisibility(View.INVISIBLE);
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
            img_link = cloudinary.url().generate(timestamp);
            Log.i("URL_FOTO", cloudinary.url().generate(timestamp));
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }

    class LearnGesture extends GestureDetector.SimpleOnGestureListener{


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(e2.getX() > e1.getX()){
            }else if(e2.getX() < e1.getX()){
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMITION_STORAGE);
            }
            return true;
        }
    }

    public void subirFoto(){
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

    //GALERIA
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

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
