package com.example.administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.usuario;
import verificador.verificar;

public class crear_cuenta extends AppCompatActivity {
    CheckBox verificar;
    EditText nombre,gmail,telefono,contra,conf_contra;
    FirebaseAuth firebase;
    alert alertas;
    FirebaseFirestore firestore;
    verificar verificador=new verificar();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        verificar=findViewById(R.id.verificado);
        firebase=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nombre=findViewById(R.id.Nombre);
        gmail=findViewById(R.id.Gmail);
        telefono=findViewById(R.id.telefono);
        contra=findViewById(R.id.password);
        conf_contra=findViewById(R.id.conf_password);
    }

    public void crear(View view) {
        if (verificar.isChecked()){
            crear_cuenta();
            System.out.println("entro");
        }else{
            System.out.println("no");
        }


    }
public void crear_cuenta(){
        if (verificador.verificacion_crear_cuenta(nombre,gmail,telefono,contra,conf_contra)){
            firebase.createUserWithEmailAndPassword(gmail.getText().toString(),contra.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user=firebase.getCurrentUser();
                        user.sendEmailVerification();
                        enviar_datos(gmail.getText().toString());
                        limpiar();
                        alertas=new alert("se ha creado su cuenta porfavor verificar el correo enviado");
                        alertas.show(getSupportFragmentManager(),"alertas");
                    }else{
                        alertas=new alert("la cuenta no se ha creado por favor verifique que tienes internet");
                        alertas.show(getSupportFragmentManager(),"alertas");
                    }
                }
            });
        }

    }
    public void leer(View view) {
        Intent ventana=new Intent(this,MainActivity.class);
        startActivity(ventana);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void enviar_datos(String gmail){

        firestore.collection("usuarios").document(gmail).set(new usuario(telefono.getText().toString(),nombre.getText().toString(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>(),""));
    }
    public void limpiar(){
        nombre.setText("");
        gmail.setText("");
        telefono.setText("");
        contra.setText("");
        conf_contra.setText("");
        verificar.clearFocus();
    }

    public void ayudas(View view) {
        alert a=new alert("Restricciones al crear una cuenta: 1 nombre debe ser de 3 a 12 letras minusculas o mayusculas 2: un correo valido 3: numero de telefono valido 4: contraseña de entre 6 a 12 caracteres con un numero");
        a.show(getSupportFragmentManager(),"alerta");
    }
}