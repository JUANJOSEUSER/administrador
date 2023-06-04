package com.example.administrador;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.ColorSpace;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link lista_segumiento#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lista_segumiento extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore firestore;
    RecyclerView lista;

    public lista_segumiento() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lista_segumiento.
     */
    // TODO: Rename and change types and number of parameters
    public static lista_segumiento newInstance(String param1, String param2) {
        lista_segumiento fragment = new lista_segumiento();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View a=inflater.inflate(R.layout.fragment_lista_segumiento, container, false);
        firestore=FirebaseFirestore.getInstance();
        lista=a.findViewById(R.id.listado);
        LinearLayoutManager lineal2 = new LinearLayoutManager(getContext());
        lista.setLayoutManager(lineal2);
       productos prod = new productos();
       firestore.collection("pedidos").document("pedidos_admin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
           @Override
           public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String>li=new ArrayList<>();
                li= (ArrayList<String>) documentSnapshot.get("pedidos");
                prod.setUnicos(li);
               lista.setAdapter(prod);
               prod.notifyDataSetChanged();
           }
       });
        return a;
    }


    private class productos extends RecyclerView.Adapter<productos.Adaptador> {


        @NonNull
        @Override
        public productos.Adaptador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new productos.Adaptador(getLayoutInflater().inflate(R.layout.histrorial_proceso, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull productos.Adaptador holder, int position) {
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

        @SuppressLint("MissingInflatedId")
        public void mostrar(int pos){
            AlertDialog.Builder s=new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View view =getLayoutInflater().inflate(R.layout.pedidos_admin,null);
            TextView corr=view.findViewById(R.id.Correo_admin);
            TextView id_ped=view.findViewById(R.id.pedido_admin);
            TextView tef_id=view.findViewById(R.id.Telefono_admin);
            TextView seg_id=view.findViewById(R.id.seguimiento_admin);
            TextView costo=view.findViewById(R.id.Nombre_admin);
            TextView dirrecion=view.findViewById(R.id.dirrecion_admin);
            TextView pago=view.findViewById(R.id.pago_admin);
            ImageView añadir=view.findViewById(R.id.cambiar_segumiento);

            RecyclerView lista=view.findViewById(R.id.listado_hiden);
            LinearLayoutManager lineal2 = new LinearLayoutManager(getContext());
            lista.setLayoutManager(lineal2);
            productos2 prod2 = new productos2();

            firestore.collection("nuevos pedidos").document(unicos.get(pos)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    corr.setText(documentSnapshot.getString("correo"));
                    id_ped.setText("numero de pedido#"+documentSnapshot.get("id"));
                    tef_id.setText(documentSnapshot.getString("telefono"));
                    seg_id.setText(documentSnapshot.getString("segumiento"));
                    costo.setText(documentSnapshot.getString("nombre"));
                    dirrecion.setText(documentSnapshot.getString("dirrecion"));
                    pago.setText(documentSnapshot.getString("pago")+"€");
                    ArrayList<String>li=new ArrayList<>();
                    li= (ArrayList<String>) documentSnapshot.get("productos");
                    prod2.setUnicos(li);
                    lista.setAdapter(prod2);
                    prod2.notifyDataSetChanged();
                }
            });
            añadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        CharSequence opciones[] = {"En proceso", "En logistica", "Proceso de entrega a trasportista", "Entregado a trasportista", "En reparto", "Entregado","Finalizar"};
                        AlertDialog.Builder ventana = new AlertDialog.Builder(getActivity());
                        ventana.setSingleChoiceItems(opciones, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                seg_id.setText(opciones[which]);
                            }
                        }).setPositiveButton("Acceptar", null).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                seg_id.setText("");
                            }
                        });

                        ventana.show();
                    }

            });
            s.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (seg_id.getText().toString().equals("Finalizar")){
                        sumar_finalizado(pos);
                       DocumentReference re= firestore.collection("usuarios").document(corr.getText().toString());
                               re.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ArrayList<String>lista=new ArrayList<>();
                                lista= (ArrayList<String>) documentSnapshot.get("trasporte");
                                lista.remove(unicos.get(pos));
                                re.update("trasporte",lista).isSuccessful();
                            }
                        });
                        firestore.collection("nuevos pedidos").document(unicos.get(pos)).delete();

                    }else{
                        DocumentReference ref= firestore.collection("nuevos pedidos").document(unicos.get(pos));
                        ref.update("segumiento",seg_id.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    alert alerta=new alert("se ha actualizado el segumiento");
                                    alerta.show(getParentFragmentManager(),"alerta");
                                }
                            }
                        });
                    }

                }
            });
            s.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });


            s.setView(view);
            s.show();
        }
        public void sumar_finalizado(int o){
            firestore.collection("pedidos").document("finalizados").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a= documentSnapshot.getLong("numero").intValue();

                    a++;

                    DocumentReference ad= firestore.collection("pedidos").document("finalizados");
                    ad.update("numero",a).isSuccessful();
                }
            });
            firestore.collection("pedidos").document("emision").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a=  documentSnapshot.getLong("numero").intValue();

                    a--;

                    DocumentReference ad= firestore.collection("pedidos").document("emision");
                    ad.update("numero",a).isSuccessful();
                }
            });
            firestore.collection("pedidos").document("pedidos_admin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<String>lista=new ArrayList<>();
                    lista= (ArrayList<String>) documentSnapshot.get("pedidos");
                    lista.remove(unicos.get(o));
                  DocumentReference f= firestore.collection("pedidos").document("pedidos_admin");
                  f.update("pedidos",lista).isSuccessful();
                }
            });


        }

        private class Adaptador extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView p1, p2, p3;



            public Adaptador(@NonNull View itemView) {
                super(itemView);

                p1 = itemView.findViewById(R.id.Nombre_proceso);
                p2 = itemView.findViewById(R.id.Precio_proceso);
                p3 = itemView.findViewById(R.id.Seguimiento_proceso);
                itemView.setOnClickListener(this);

            }

            public void imprimir(int position) {
                firestore.collection("nuevos pedidos").document(unicos.get(position)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        p1.setText("Pedido# "+documentSnapshot.getString("id"));
                        if (documentSnapshot.getString("segumiento").equals("Entregado")){
                            p3.setTextColor(Color.rgb(252, 3, 44));
                        }else{
                            p3.setTextColor(Color.rgb(0, 255, 34));
                        }
                        p3.setText(documentSnapshot.getString("segumiento"));
                        p2.setText(documentSnapshot.getString("pago")+"€");
                    }
                });
            }









            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());


            }
        }
    }
    private class productos2 extends RecyclerView.Adapter<productos2.Adaptador> {


        @NonNull
        @Override
        public productos2.Adaptador onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new productos2.Adaptador(getLayoutInflater().inflate(R.layout.historial2, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull productos2.Adaptador holder, int position) {
            holder.imprimir(position);
        }


        ArrayList<String> unicos = new ArrayList<>();




        public void setUnicos(ArrayList<String> unicos) {
            this.unicos = unicos;
        }

        @Override
        public int getItemCount() {

            return unicos.size();
        }


        public void mostrar(int pos){

        }

        private class Adaptador extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView p1, p2, p3;



            public Adaptador(@NonNull View itemView) {
                super(itemView);
                p1 = itemView.findViewById(R.id.historial_producto);
                itemView.setOnClickListener(this);

            }

            public void imprimir(int position) {
                p1.setText(unicos.get(position));

            }
            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());


            }
        }
    }

}