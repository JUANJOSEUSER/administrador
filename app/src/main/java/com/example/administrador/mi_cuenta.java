package com.example.administrador;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.PostalAddress;
import com.hbb20.CountryCodePicker;

import modelo.usuario;
import verificador.verificar;

public class mi_cuenta extends AppCompatActivity {
FirebaseFirestore firestore;
FirebaseAuth auth;
FirebaseUser user;
verificar vef=new verificar();

EditText name,tel,dire,ciudad,postal;
usuario usuario=new usuario();
CountryCodePicker cp;
    @SuppressLint("WrongViewCast")
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
        ciudad=findViewById(R.id.editar_ciudad);
        postal=findViewById(R.id.codigo_postal);

        cp=findViewById(R.id.ccp);
       DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
       ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                usuario us=new usuario();
                us=documentSnapshot.toObject(usuario.class);
               setUsuario(us);
                name.setText(us.getNombre());
                tel.setText(us.getTelefono());
                dire.setText(us.getDirrecion());
            }
        });


    }

    public modelo.usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(modelo.usuario usuario) {
        this.usuario = usuario;
    }

    public void cancel(View view) {
        finish();
    }

    public void aceptar(View view) {
        if (vef.verificar_cambio_cuenta(name,tel,dire,postal,ciudad)==true){
            DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
            ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    usuario us=new usuario();
                    us=documentSnapshot.toObject(usuario.class);
                   us.setNombre(name.getText().toString());
                   us.setTelefono(tel.getText().toString());
                   us.setDirrecion(dire.getText().toString()+" pais:"+cp.getSelectedCountryName()+" codigo postal: "+postal.getText().toString()+" ciudad: "+ciudad.getText().toString());
                   firestore.collection("usuarios").document(user.getEmail()).set(us);
                }
            });
        }


    }
}