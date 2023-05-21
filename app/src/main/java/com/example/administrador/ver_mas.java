package com.example.administrador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    TextView nombre,precio,talla,descripcion;
    ImageView imagen;
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

base.child("productos").child(sacar_referencias()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
if (snapshot.exists()){
    nombre.setText(snapshot.child("nombre").getValue().toString());
    precio.setText(snapshot.child("precio").getValue().toString()+" Col");
    talla.setText("Talla:"+snapshot.child("talla").getValue().toString());
    descripcion.setText(snapshot.child("descripcion").getValue().toString());
}
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
leer(sacar_referencias(),imagen);
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
}