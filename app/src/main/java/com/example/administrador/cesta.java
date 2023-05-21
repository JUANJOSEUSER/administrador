package com.example.administrador;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.paypal.android.sdk.payments.PayPalConfiguration;

import java.util.ArrayList;
import java.util.Collections;

import modelo.usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link cesta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class cesta extends Fragment {

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
    int paypalcode=7171;
    public static String clave="AZaouBk2jcTFbDk-ajw-IwusjijC9aBwQibg7iJ_03M-j2CDvADQHCnIGP9JV5JpS-jl2Ju6vO1zGlbT";
    PayPalConfiguration config=new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(clave);
    FirebaseUser user;
    RecyclerView cesta;
    ArrayList<String> lista = new ArrayList<>();
    ArrayList<String> unico = new ArrayList<>();
    int precio_guardado=0;

    public cesta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment cesta.
     */
    // TODO: Rename and change types and number of parameters
    public static cesta newInstance(String param1, String param2) {
        cesta fragment = new cesta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
Button boton_añadir;
    TextView precio_final;
    productos prod;
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

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        refenrencia = FirebaseDatabase.getInstance().getReference();
        View a = inflater.inflate(R.layout.fragment_cesta, container, false);
        cesta = a.findViewById(R.id.cesta_items);
        LinearLayoutManager lineal = new LinearLayoutManager(getContext());
        cesta.setLayoutManager(lineal);
       prod = new productos();
        boton_añadir=a.findViewById(R.id.button2);
        precio_final=a.findViewById(R.id.precio_final);


        firestore.collection("usuarios").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                lista = (ArrayList<String>) documentSnapshot.get("productos");
                Collections.sort(lista);
                unico=unicos(lista);
                prod.setListas(lista);
                prod.setUnicos(unico);
                cesta.setAdapter(prod);
                prod.notifyDataSetChanged();
            }
        });
        boton_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("usuarios").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        usuario us=new usuario();
                        us=documentSnapshot.toObject(usuario.class);
                        if (us.getProductos().size()!=0){
                            Intent a=new Intent(getActivity(),pagos.class);
                            a.putExtra("precio",precio_guardado);
                            startActivity(a);
                        }else{
                            alert alerta=new alert("no tienes productos elige uno para poder comprar");
                            alerta.show(getParentFragmentManager(),"alerta");
                        }
                    }
                });


            }
        });


        return a;
    }

    public void consulta() {

    }
    public ArrayList<String> unicos(ArrayList<String> a) {
        if (a!=null&&a.size()!=0){
            ArrayList<String> unicos = new ArrayList<>();
            unicos.add(a.get(0));
            int d = 0;
            for (int i = 0; i < a.size(); i++) {
                if (!a.get(i).equals(unicos.get(d))) {
                    unicos.add(a.get(i));
                    d++;
                }

            }
            return unicos;
        }

        return new ArrayList<>();
    }
    private class productos extends RecyclerView.Adapter<cesta.productos.Adaptador> {


        @NonNull
        @Override
        public Adaptador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new productos.Adaptador(getLayoutInflater().inflate(R.layout.layout_items2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Adaptador holder, int position) {
            holder.imprimir(position);
        }

        ArrayList<String> listas = new ArrayList<String>();
        ArrayList<String> unicos = new ArrayList<>();

        ArrayList<String> precio = new ArrayList<>();


        public void setListas(ArrayList<String> listas) {
            this.listas = listas;
        }

        public void setUnicos(ArrayList<String> unicos) {
            this.unicos = unicos;
        }

        @Override
        public int getItemCount() {

            return unicos.size();
        }

        public void mostrar(int pos) {

            String opciones[] = {"ELIMINAR", "AÑADIR", "VER MAS"};
            AlertDialog.Builder alertas = new AlertDialog.Builder(getActivity());
            alertas.setTitle(unicos.get(pos));

            alertas.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ArrayList<String> nuevalista=listas;
                   switch (opciones[which]){
                       case "ELIMINAR":
                          eliminar(nuevalista,pos);
                            prod.notifyDataSetChanged();
                           break;
                       case "AÑADIR":
                           nuevalista.add(unicos.get(pos));
                          añadir(nuevalista);
                           break;
                       case "VER MAS":
                           mandar_datos(unicos.get(pos));
                           ver_mas vista=new ver_mas();
                           vista.setArguments(getArguments());
                           getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmento,vista).addToBackStack(null).commit();
                           break;
                   }
                }
            });


            alertas.show();
//            mandar_datos(listas.get(pos));
//            ver_mas vista=new ver_mas();
//            vista.setArguments(getArguments());
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,vista).addToBackStack(null).commit();
        }
public void añadir(ArrayList<String> nuevalista){
    DocumentReference refencia=firestore.collection("usuarios").document(user.getEmail());
    refencia.update("productos",nuevalista).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                System.out.println("añadido");
                prod.notifyDataSetChanged();
            }
        }
    });
}
public void eliminar(ArrayList<String>n,int pos){
    for (int i = 0; i <n.size() ; i++) {
        if (n.get(i).equals(unicos.get(pos))){
            n.remove(i);
            break;
        }
    }
    DocumentReference refencia=firestore.collection("usuarios").document(user.getEmail());
    refencia.update("productos",n).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                System.out.println("se ha eliminado");
                prod.notifyDataSetChanged();
            }
        }
    });
}
        private class Adaptador extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView p1, p2, p3;
            ImageView img;


            public Adaptador(@NonNull View itemView) {
                super(itemView);
                p1 = itemView.findViewById(R.id.cesta_nombre);
                p2 = itemView.findViewById(R.id.cesta_precio);
                p3 = itemView.findViewById(R.id.numero_cesta);
                img = itemView.findViewById(R.id.cesta_img);
                itemView.setOnClickListener(this);

            }

            public void imprimir(int position) {
                int ancho = 300;
                int alto = 300;

                consulta(listas);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
                leer(unicos.get(position), img);
                img.setLayoutParams(params);
                p1.setText(unicos.get(position));
                p3.setText(String.valueOf(conteo(listas,unicos.get(position))));


                refenrencia.child("productos").child(unicos.get(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            p2.setText(snapshot.child("precio").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


            }

            int preci=0;
            int resultado=0;
            public void consulta(ArrayList<String>a){

                for (int i = 0; i <a.size() ; i++) {
                    refenrencia.child("productos").child(listas.get(i)).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                String a=snapshot.child("precio").getValue().toString();
                                preci=Integer.parseInt(a);
                                resultado=resultado+preci;
                                precio_guardado=resultado;
                                    precio_final.setText("Precio final: "+String.valueOf(resultado)+"€");



                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }


            }

            public int conteo(ArrayList<String> f, String a) {
                int cont = 0;
                for (int i = 0; i < f.size(); i++) {
                    if (f.get(i).equals(a)) {
                        cont++;
                    }
                }
                return cont;
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
