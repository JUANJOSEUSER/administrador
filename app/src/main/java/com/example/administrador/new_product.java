package com.example.administrador;

import static android.content.Intent.CATEGORY_APP_GALLERY;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import animaciones.scale_anim;
import modelo.model_product;
import verificador.verificar;

public class new_product extends AppCompatActivity {
    alert alertas;
    ArrayList<String> num_product = new ArrayList<>();
    scale_anim animacion=new scale_anim();


    EditText nombre, Descripcion, precio, Talla;
    StorageReference storage;
    ImageView imagen,conf_nom,conf_des,conf_precio,conf_talla;
    model_product modelo;

    private DatabaseReference Database;
// ...

    private static final int CATEGORY_APP_GALLERY = 1;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        nombre = findViewById(R.id.nombre);
        precio = findViewById(R.id.precio);
        Descripcion = findViewById(R.id.descripcion);
        Talla = findViewById(R.id.talla);
        imagen = findViewById(R.id.img_product);
        storage = FirebaseStorage.getInstance().getReference();
        Database = FirebaseDatabase.getInstance().getReference();
        conf_nom=findViewById(R.id.conf_nombre);
        conf_des=findViewById(R.id.conf_descripcion);
        conf_precio=findViewById(R.id.conf_precio);
        conf_talla=findViewById(R.id.conf_talla);
        select();
    }

    public void setNum_product(ArrayList<String> num_product) {
        this.num_product = num_product;
    }



    public void select() {
        Database.child("num_product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> md = new ArrayList<>();
                    md = (ArrayList<String>) snapshot.child("Nombres").getValue();
                    setNum_product(md);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void insert2() {
        num_product.add(nombre.getText().toString());
        Database.child("num_product").child("Nombres").setValue(num_product);
    }

    public void insert() {
        verificar vf=new verificar();
        vf.verificador(nombre, Descripcion,precio,Talla,conf_nom,conf_precio,conf_des,conf_talla);
        modelo = new model_product(nombre.getText().toString(), Descripcion.getText().toString(), Talla.getText().toString(), precio.getText().toString());

        Database.child("productos").child(nombre.getText().toString()).setValue(modelo);
    }

    @SuppressLint("ResourceType")
    public void añadir(View view) {
        animacion.animar(this,view);
        insert();


        select();
        for (int i = 0; i <2 ; i++) {
           select();
        }
        System.out.println(num_product.size());
        if (num_product.size() != 0) {
            insert2();
        }else if(num_product.size()==0){
            insert2();
        }



    }




    public void img(View view) {
        if (nombre.getText().toString().matches("[a-zA-Z ]{8,18}$")&&nombre.getText().toString()!=null){
            Intent img = new Intent(Intent.ACTION_PICK);
            img.setType("image/*");
            startActivityForResult(img, CATEGORY_APP_GALLERY);
        }else{
            alertas=new alert("Debes colocar un nombre para el amagen");
            alertas.show(getSupportFragmentManager(),"dialogo");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_APP_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            StorageReference file = storage.child("productos/" + nombre.getText().toString());
            file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagen.setImageURI(uri);
                    Toast.makeText(new_product.this, "se subio", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void selec_talla(View view) {
        CharSequence opciones[] = {"XS", "S", "M", "L", "XL", "XXL"};
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
        ventana.setSingleChoiceItems(opciones, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Talla.setText(opciones[which]);
            }
        }).setPositiveButton("Acceptar", null).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Talla.setText("");
            }
        });

        ventana.show();
    }

    public void cancelar(View view) {
        animacion.animar(this,view);
        finish();
    }
}