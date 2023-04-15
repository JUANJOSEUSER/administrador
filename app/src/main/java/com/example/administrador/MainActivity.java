package com.example.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import animaciones.scale_anim;
import verificador.verificar;


public class MainActivity extends AppCompatActivity {
    scale_anim animacion=new scale_anim();
    FirebaseAuth firebase;
    EditText gmail,password;
    final String usuario="super-root@root.es";
    final String contraseña="root1234";
    verificar vef=new verificar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase = FirebaseAuth.getInstance();
        gmail=findViewById(R.id.usuario);
        password=findViewById(R.id.pass);
//        si funciona
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void menu(View view) {
        animacion.animar(getApplication(),view);
        ingresar();
//


    }
    public void ingresar(){
        Intent a=new Intent(this,inicio_adm.class);
        startActivity(a);

    }
public boolean super_usuario(String usuario,String contraseña){
        if (usuario.equals(this.usuario)&&contraseña.equals(this.contraseña)){
            return true;
        }
        return false;
}
    public void nueva_contraseña(View view) {
        animacion.animar(getApplication(),view);
        Intent a=new Intent(this,restablecer.class);
        startActivity(a);

    }
}