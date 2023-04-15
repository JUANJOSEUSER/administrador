package com.example.administrador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import modelo.model_product;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vista_productos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vista_productos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference base;
    RecyclerView vista;

    public vista_productos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vista_productos.
     */
    // TODO: Rename and change types and number of parameters
    public static vista_productos newInstance(String param1, String param2) {
        vista_productos fragment = new vista_productos();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View a=inflater.inflate(R.layout.fragment_vista_productos, container, false);
        vista = a.findViewById(R.id.vista);
        LinearLayoutManager lineal = new LinearLayoutManager(getContext());
        vista.setLayoutManager(lineal);
        productos prod=new productos();
        vista.setAdapter(prod);
        base = FirebaseDatabase.getInstance().getReference();
        base.child("num_product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ArrayList<String> d=new ArrayList<>();
                    prod.setListas((ArrayList) snapshot.child("Nombres").getValue());
                    prod.notifyDataSetChanged();


                }else{
                    System.out.println("emtro");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
         return a;
    }
    private class productos extends RecyclerView.Adapter<productos.Adaptador> {
        @NonNull
        @Override
        public productos.Adaptador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new productos.Adaptador(getLayoutInflater().inflate(R.layout.layout_items, parent, false));
        }
        ArrayList<String> listas=new ArrayList<String>();
        ArrayList<model_product>nuevos=new ArrayList<>();
        public void setListas(ArrayList<String> listas) {
            this.listas = listas;
        }

        @Override
        public void onBindViewHolder(@NonNull productos.Adaptador holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {

            return listas.size();
        }

        public void mostrar(int pos){
            System.out.println(listas.get(pos));
            mandar_datos(listas.get(pos));
            ver_mas vista=new ver_mas();
            vista.setArguments(getArguments());
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.escenario,vista).addToBackStack(null).commit();
        }

        private class Adaptador extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView p1, p2;
            ImageView img;



            public Adaptador(@NonNull View itemView) {
                super(itemView);
                p1 = itemView.findViewById(R.id.nom_product);
                p2 = itemView.findViewById(R.id.precio_product);
                img = itemView.findViewById(R.id.product_img);
                itemView.setOnClickListener(this);

            }

            public void imprimir(int position) {
                int ancho = 200;
                int alto = 170;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);


                leer(listas.get(position),img);
                img.setLayoutParams(params);
                p1.setText(listas.get(position));
                base.child("productos").child(listas.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            p2.setText(snapshot.child("precio").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });





            }


            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());


            }
        }
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
    public void mandar_datos(String producto){//cada vez que se inicia seccion se crea un xml donde guardaremos datos en memoria
        SharedPreferences librito= getActivity().getSharedPreferences("intermediador", Context.MODE_PRIVATE);//se coloca el nombre del xml y el context si quiere ser privado o de acceso restringido
        SharedPreferences.Editor libro=librito.edit();//editor hace la funcion de poder escribir en el xml mandadole la clave y el valor
        libro.putString("producto",producto);

        libro.commit();
    }
}