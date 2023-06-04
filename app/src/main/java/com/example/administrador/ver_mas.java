package com.example.administrador;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import modelo.model_product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ver_mas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ver_mas extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference base;
    EditText nombre,precio,talla,descripcion;
    ImageView imagen;
    alert alertas;
    StorageReference storage;
    private static final int CATEGORY_APP_GALLERY = 1;
    public ver_mas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ver_mas.
     */
    // TODO: Rename and change types and number of parameters
    public static ver_mas newInstance(String param1, String param2) {
        ver_mas fragment = new ver_mas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public String sacar_referencias() {//abrimos el archivo xml y sacamos la referencia de usuario
        SharedPreferences referencia = getActivity().getSharedPreferences("intermediador", Context.MODE_PRIVATE);
        return referencia.getString("producto", null);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View a=inflater.inflate(R.layout.fragment_ver_mas, container, false);
        base = FirebaseDatabase.getInstance().getReference();
        nombre=a.findViewById(R.id.nombre_compra);
        precio=a.findViewById(R.id.precio_compra);
        talla=a.findViewById(R.id.talla_compra);
        descripcion=a.findViewById(R.id.descrip);
        imagen=a.findViewById(R.id.vista_previa);
        storage=FirebaseStorage.getInstance().getReference();



base.child("productos").child(sacar_referencias()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
if (snapshot.exists()){
    nombre.setText(snapshot.child("nombre").getValue().toString());
    precio.setText(snapshot.child("precio").getValue().toString()+" Col");
    talla.setText(snapshot.child("talla").getValue().toString());
    descripcion.setText(snapshot.child("descripcion").getValue().toString());
}
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
        a.findViewById(R.id.eliminar_producto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base.child("productos").child(sacar_referencias()).removeValue().isSuccessful();
                base.child("num_product").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ArrayList<String> md = new ArrayList<>();
                            md = (ArrayList<String>) snapshot.child("Nombres").getValue();
                            md.remove(sacar_referencias());
                            base.child("num_product").child("Nombres").setValue(md);
                            alertas=new alert("se ha eliminado correctamente el producto");
                            alertas.show(getParentFragmentManager(),"alerta");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        a.findViewById(R.id.actualizar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                base.child("productos").child(sacar_referencias()).removeValue();
                base.child("num_product").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                            ArrayList<String>a=new ArrayList<>();
//                            a= (ArrayList<String>) snapshot.child("Nombres").getValue();
//                            a.remove()
//                            a.add(nombre.getText().toString());
//                            base.child("num_product").child("Nombres").setValue(a);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                model_product a=new model_product(nombre.getText().toString(),descripcion.getText().toString(),talla.getText().toString(),precio.getText().toString());
                base.child("productos").child(nombre.getText().toString()).setValue(a);
                alertas=new alert("Se ha actualizado el producto");
            }
        });
leer(sacar_referencias(),imagen);
a.findViewById(R.id.tallas).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        CharSequence opciones[] = {"XS", "S", "M", "L", "XL", "XXL"};
        AlertDialog.Builder ventana = new AlertDialog.Builder(getActivity());
        ventana.setSingleChoiceItems(opciones, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                talla.setText(opciones[which]);
            }
        }).setPositiveButton("Acceptar", null).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                talla.setText("");
            }
        });

        ventana.show();
    }

});
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent img = new Intent(Intent.ACTION_PICK);
                img.setType("image/*");
                startActivityForResult(img, CATEGORY_APP_GALLERY);
            }
        });
        return a;
    }
    public void leer(String a, ImageView v) {
        final long ONE_MEGABYTE = 1024 * 1024;
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

            StorageReference file = storage.child("productos/" + nombre.getText().toString());
            file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagen.setImageURI(uri);
                    Toast.makeText(getActivity(), "se subio", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}