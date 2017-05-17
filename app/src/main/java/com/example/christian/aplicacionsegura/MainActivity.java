package com.example.christian.aplicacionsegura;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.christian.aplicacionsegura.Fragments.EstadisticasFragment;
import com.example.christian.aplicacionsegura.Fragments.MapaFragment;
import com.example.christian.aplicacionsegura.Fragments.MuroFragment;
import com.example.christian.aplicacionsegura.Fragments.PerfilFragment;
import com.example.christian.aplicacionsegura.Fragments.PreferenciasFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Fragment fragment;
    private FragmentManager fragmentManager;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.inflateMenu(R.menu.navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id){
                    case R.id.navigation_mapa:
                        fragment = new MapaFragment();
                        break;
                    case R.id.navigation_perfil:
                        fragment = new PerfilFragment();
                        break;
                    case R.id.navigation_muro:
                        fragment = new MuroFragment();
                        break;
                    case R.id.navigation_estadisticas:
                        fragment = new EstadisticasFragment();
                        break;
                    case R.id.navigation_preferencias:
                        fragment = new PreferenciasFragment();
                        break;

                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

        fragment = new MapaFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }

}
