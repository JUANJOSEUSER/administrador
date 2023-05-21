package com.example.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import animaciones.scale_anim;
import verificador.verificar;


public class MainActivity extends AppCompatActivity {
    scale_anim animacion=new scale_anim();
    final String variables="usuarios",admin="administradores";
    FirebaseAuth firebase;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    EditText gmail,password;
    FirebaseUser users=null;
    final String usuario="super-root@root.es";
    final String contraseña="root1234";

    FirebaseFirestore documentos;
    verificar vef=new verificar();
    alert alerta;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebase = FirebaseAuth.getInstance();
        gmail=findViewById(R.id.usuario);
        password=findViewById(R.id.pass);
        documentos=FirebaseFirestore.getInstance();

//        si funciona
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);



    }

    @Override
    protected void onStart() {
        super.onStart();
        users=firebase.getCurrentUser();
        if (users!=null){
            vista_admin(users.getEmail());
            vista_usuario(users.getEmail());
        }
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
conuslta();



    }


    public void conuslta() {
        firebase.signInWithEmailAndPassword(gmail.getText().toString(),password.getText().toString()).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    users=firebase.getCurrentUser();
                    vista_admin(gmail.getText().toString());
                    vista_usuario(gmail.getText().toString());

                }else{
                    alerta=new alert("compruebe su correo o contraseña");
                    alerta.show(getSupportFragmentManager(),"alerta");
                }
            }
        });
//
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

    public void crearcuenta(View view) {
        animacion.animar(getApplication(),view);
        Intent a=new Intent(this,crear_cuenta.class);
        startActivity(a);


    }
    public void vista_usuario(String user){

       firestore.collection("usuarios").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 Intent a=new Intent(getApplication(),main_usuario.class);
                 startActivity(a);
            }
        });


    }
    public void vista_admin(String user){
        documentos.collection(admin).document(variables).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> a = (ArrayList<String>) documentSnapshot.get("admin");

                for (int i = 0; i < a.size(); i++) {
                    if (user.equals(a.get(i))) {
                        ingresar();
                    }
                }

            }
        });
    }
}