package com.example.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import modelo.model_product;

public class inicio_adm extends AppCompatActivity {
    DrawerLayout scene;
    NavigationView navegador;
    FirebaseAuth firebase;
    ActionBarDrawerToggle accion;

    DatabaseReference base;
    DatabaseReference base2;
    RecyclerView vista;
    ArrayList<String>name=new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_adm);
        scene = findViewById(R.id.escenario);
        navegador = findViewById(R.id.lateral);
        accion = new ActionBarDrawerToggle(this, scene, R.string.abrir, R.string.cerrar);
        scene.addDrawerListener(accion);
        accion.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        base = FirebaseDatabase.getInstance().getReference();

//        ocultar navegacion superior
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        ////////////////////////////////////////////
        navegador.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.inicio_menu:
                        FragmentManager fm=getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.escenario,new vista_productos()).commit();
                        break;
                    case R.id.catalogo_menu:

                        break;
                    case R.id.config_menu:
                        break;
                    case R.id.estadistica_menu:
                        break;
                    case R.id.cerrar_menu:
                        break;
                }
                return false;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_product, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (accion.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.getItemId() == R.id.nuevo) {
            cambio();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (scene.isDrawerOpen(GravityCompat.START)) {
            scene.isDrawerOpen(GravityCompat.START);

        } else {

        }
        super.onBackPressed();
    }

    public void cambio() {
        Intent i = new Intent(this, new_product.class);
        startActivity(i);
    }

    //    metodos para ocultar barras de navegacion
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
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }



}