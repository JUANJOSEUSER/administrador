package com.example.administrador;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class inicio_adm extends AppCompatActivity {
    DrawerLayout scene;
    NavigationView navegador;
    FirebaseAuth firebase;
    FirebaseUser usuario;
    Toolbar bar;

    ActionBarDrawerToggle accion;
    StorageReference storage;
    ImageView ima;
    private static final int CATEGORY_APP_GALLERY = 1;
    DatabaseReference base;
    DatabaseReference base2;
    RecyclerView vista;
    ArrayList<String> name = new ArrayList<>();


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_adm);
        scene = findViewById(R.id.escenario);
        navegador = findViewById(R.id.lateral);
        bar=findViewById(R.id.toolbar2);

        accion=new ActionBarDrawerToggle(this,scene,0,0);

        scene.addDrawerListener(accion);

        accion.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        base = FirebaseDatabase.getInstance().getReference();
        firebase = FirebaseAuth.getInstance();
        usuario=firebase.getCurrentUser();
        storage= FirebaseStorage.getInstance().getReference();
        FragmentManager fm = getSupportFragmentManager();


        fm.beginTransaction().replace(R.id.frame, new vista_admin_inicio()).commit();


        navegador.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fm = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.inicio_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new vista_productos()).commit();
                        break;
                    case R.id.catalogo_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new lista_segumiento()).commit();
                        break;
                    case R.id.config_menu:
                        System.out.println("entra");
                        fm.beginTransaction().replace(R.id.frame, new configuraciones_admin()).commit();
                        break;
                    case R.id.estadistica_menu:
                        fm.beginTransaction().replace(R.id.frame, new estadistica()).commit();
                        break;
                    case R.id.cerrar_menu:
                        finish();
                        firebase.signOut();
                        break;
                }
                scene.closeDrawers();
                return false;
            }

        });
        TextView name_user=navegador.getHeaderView(0).findViewById(R.id.Nombre_perfil_sup);
//
        name_user.setText(usuario.getEmail());


    }

    public void leer(String a, ImageView v) {
        final long ONE_MEGABYTE = 624 * 624;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imge = storageReference.child("productos/" + a);
        imge.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                v.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_APP_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            StorageReference file = storage.child("productos/" + usuario.getEmail());
            file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ima.setImageURI(uri);
                    Toast.makeText(getApplication(), "se subio", Toast.LENGTH_SHORT).show();
                }
            });
        }
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