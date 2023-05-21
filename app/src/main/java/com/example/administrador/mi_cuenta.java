package com.example.administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import modelo.usuario;

public class mi_cuenta extends AppCompatActivity {
FirebaseFirestore firestore;
FirebaseAuth auth;
FirebaseUser user;
EditText name,tel,dire;
usuario us=new usuario();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        name=findViewById(R.id.editar_nombre);
        tel=findViewById(R.id.editar_telefono);
        dire=findViewById(R.id.editar_dirrecion);
       DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
       ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                us=documentSnapshot.toObject(usuario.class);
                name.setText(us.getNombre());
                tel.setText(us.getTelefono());
                dire.setText(us.getDirrecion());
            }
        });

    }

    public void cancel(View view) {
        finish();
    }

    public void aceptar(View view) {

    }
}