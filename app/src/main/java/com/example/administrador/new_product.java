package com.example.administrador;

import static android.content.Intent.CATEGORY_APP_GALLERY;



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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.model_product;

public class new_product extends AppCompatActivity {
    FirebaseAuth myAuth;
    alert alertas;
    ArrayList<String> products=new ArrayList<>();
    EditText nombre,Descripcion,precio,Talla;
    FirebaseFirestore registro;
    StorageReference storage;
    ImageView imagen;
    model_product modelo;
    String a;
    private DatabaseReference Database;
// ...

    private static final int CATEGORY_APP_GALLERY=1;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
//        registro= FirebaseFirestore.getInstance();
        nombre=findViewById(R.id.nombre);
        precio=findViewById(R.id.precio);
        Descripcion=findViewById(R.id.descripcion);
        imagen=findViewById(R.id.img_product);
        storage= FirebaseStorage.getInstance().getReference();
        Database = FirebaseDatabase.getInstance().getReference();

    }
    public void cloud(){

        Map<String, Object> product = new HashMap<>();
        product.put("Nombre", nombre.getText().toString());
        product.put("Precio", precio.getText().toString());
        product.put("Descripcion", Descripcion.getText().toString());

        registro.collection("product_info").document(nombre.getText().toString()).set(product);
    }
    public void cloud2(){
        products.add(nombre.getText().toString());
        Map<String, Object> product = new HashMap<>();
        product.put("Nombres",products);
        registro.collection("product_info").document("juan").set(product);
    }

//    public void regresar_info(){
//        registro.collection("product_info").document("juan").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()){
//                    a=documentSnapshot.getString("Nombre");
//                }else{
//                    alertas=new alert("no hay datos por ver");
//                }
//            }
//        });
//    }
public void insert(){
        modelo=new model_product(nombre.getText().toString(),Descripcion.getText().toString(),"M",Float.parseFloat(precio.getText().toString()));
    Database.child("productos").child(nombre.getText().toString()).setValue(modelo);
}
    public void añadir(View view) {
        insert();
//        cloud();
//        cloud2();
    }


    public void img(View view) {
        Intent img=new Intent(Intent.ACTION_PICK);
        img.setType("image/*");
        startActivityForResult(img,CATEGORY_APP_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CATEGORY_APP_GALLERY&&resultCode==RESULT_OK){
            Uri uri=data.getData();

            StorageReference file=storage.child("productos/"+nombre.getText().toString());
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
        CharSequence opciones[]={"XS","S","M","L","XL","XXL"};
        AlertDialog.Builder ventana=new AlertDialog.Builder(this);
        ventana.setSingleChoiceItems(opciones, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println(opciones[which]);
            }
        });
        ventana.show();
    }
}