package com.example.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.rpc.context.AttributeContext;

@ExperimentalBadgeUtils public class main_usuario extends AppCompatActivity {


BottomNavigationView navegador;

    @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usuario);
        navegador = findViewById(R.id.navegador_usuario);
        renplace(new inicio_usuario());

        navegador.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.inicio_usuario:
                        renplace(new inicio_usuario());
                        break;
                    case R.id.inicio:
                        renplace(new productos_usuarios());
                        break;
                    case R.id.cesta:
                        renplace(new cesta());

                        break;
                    case R.id.perfil:
                        renplace(new perfil_usuario());

                        break;
                }
                return false;
            }
        });




    }
    public static int dp(Context a,int d){
        Resources r=a.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,d,r.getDisplayMetrics()));

    }
    public void renplace(Fragment a){
        FragmentManager d=getSupportFragmentManager();
        FragmentTransaction ad=d.beginTransaction();
        ad.replace(R.id.fragmento,a);
        ad.addToBackStack(null);
        ad.commit();
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}