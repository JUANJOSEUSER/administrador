package com.example.administrador;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link productos_usuarios#newInstance} factory method to
 * create an instance of this fragment.
 */
public class productos_usuarios extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference firebasedata;




    public productos_usuarios() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment productos_usuarios.
     */
    // TODO: Rename and change types and number of parameters
    public static productos_usuarios newInstance(String param1, String param2) {
        productos_usuarios fragment = new productos_usuarios();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    RecyclerView a;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productos_usuarios, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        firebasedata=FirebaseDatabase.getInstance().getReference();
        a = view.findViewById(R.id.vista_productos_usuario);
        a.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adaptador d=new adaptador();



        firebasedata.child("num_product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                d.setProductos((ArrayList<String>) snapshot.child("Nombres").getValue());
                d.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        a.setAdapter(d);



    }

    private class adaptador extends RecyclerView.Adapter<adaptador.adaptadorhold>  {

        ArrayList<String>productos=new ArrayList<>();

        public ArrayList<String> getProductos() {
            return productos;
        }

        public void setProductos(ArrayList<String> productos) {
            this.productos = productos;
        }
        public void mostrar(int pos){
            mandar_datos(productos.get(pos));
            vista_producto_compra vista=new vista_producto_compra();
            vista.setArguments(getArguments());
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmento,vista).addToBackStack(null).commit();
        }

        @NonNull
        @Override
        public adaptadorhold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new adaptadorhold(getLayoutInflater().inflate(R.layout.modelos, parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull adaptadorhold holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return productos.size();
        }




        private class adaptadorhold extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView tv1, tv2;
            ImageView im;

            public adaptadorhold(@NonNull View itemView) {
                super(itemView);
                im = itemView.findViewById(R.id.imagen_producto_usuario);
                tv1 = itemView.findViewById(R.id.Nombre_producto);
                tv2 = itemView.findViewById(R.id.Precio_producto);
                itemView.setOnClickListener(this::onClick);
            }

            public void imprimir(int pos) {
                int ancho = 424;
                int alto = 524;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
                leer(productos.get(pos), im);
                tv1.setText(productos.get(pos));
                firebasedata.child("productos").child(productos.get(pos)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tv2.setText(snapshot.child("precio").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
im.setLayoutParams(params);

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

            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());
            }
        }

    }
    public void mandar_datos(String producto){//cada vez que se inicia seccion se crea un xml donde guardaremos datos en memoria
        SharedPreferences librito= getActivity().getSharedPreferences("intermediador", Context.MODE_PRIVATE);//se coloca el nombre del xml y el context si quiere ser privado o de acceso restringido
        SharedPreferences.Editor libro=librito.edit();//editor hace la funcion de poder escribir en el xml mandadole la clave y el valor
        libro.putString("producto",producto);

        libro.commit();
    }
}