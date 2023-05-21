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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vista_producto_compra#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vista_producto_compra extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore firestore;
    DatabaseReference refenrencia;
    FirebaseAuth auth;
    FirebaseUser user;
    ArrayList<String>produc=new ArrayList<>();

    public vista_producto_compra() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vista_producto_compra.
     */
    // TODO: Rename and change types and number of parameters
    public static vista_producto_compra newInstance(String param1, String param2) {
        vista_producto_compra fragment = new vista_producto_compra();
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
TextView Nombre,precio,talla,descripcion;
    Button cesta,comprar;
    ImageView imagen;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View a=inflater.inflate(R.layout.fragment_vista_producto_compra, container, false);
        Nombre=a.findViewById(R.id.Compra_nombre);
        precio=a.findViewById(R.id.Compra_precio);
        talla=a.findViewById(R.id.Compra_talla);
        descripcion=a.findViewById(R.id.Compra_descripcion);
        imagen=a.findViewById(R.id.Compra_imagen);
        cesta=a.findViewById(R.id.añadir_cesta);
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        refenrencia= FirebaseDatabase.getInstance().getReference();
        refenrencia.child("productos").child(comienzo()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Nombre.setText(snapshot.child("nombre").getValue().toString());
                precio.setText(snapshot.child("precio").getValue().toString());
                talla.setText(snapshot.child("talla").getValue().toString());
                descripcion.setText(snapshot.child("descripcion").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
leer(comienzo(),imagen);
consulta();
cesta.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        consulta();
        produc.add(Nombre.getText().toString());
        DocumentReference reference=firestore.collection("usuarios").document(user.getEmail());
        reference.update("productos",produc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    System.out.println("actualizado");//alerta
                }
            }
        });

    }
});
        return a;
    }
    public void consulta(){
        firestore.collection("usuarios").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                produc= (ArrayList<String>) documentSnapshot.get("productos");
            }
        });
    }
    public String comienzo(){
        SharedPreferences librito= getActivity().getSharedPreferences("intermediador", Context.MODE_PRIVATE);
        return librito.getString("producto","");
    }
    public void leer(String a, ImageView v) {
        final long ONE_MEGABYTE = 800 * 800;
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
                Toast.makeText(getActivity(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

    }
}