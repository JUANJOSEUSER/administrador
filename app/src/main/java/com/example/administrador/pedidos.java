package com.example.administrador;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pedidos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pedidos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public pedidos() {
        // Required empty public constructor
    }

    ArrayList<String> listas = new ArrayList<String>();
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    alert alerta;
    RecyclerView produc, hista;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pedidos.
     */
    // TODO: Rename and change types and number of parameters
    public static pedidos newInstance(String param1, String param2) {
        pedidos fragment = new pedidos();
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
        View a = inflater.inflate(R.layout.fragment_pedidos, container, false);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        produc = a.findViewById(R.id.pedidos_emision);
        hista = a.findViewById(R.id.historial);
        LinearLayoutManager lineal = new LinearLayoutManager(getContext());
        LinearLayoutManager lineal2 = new LinearLayoutManager(getContext());
        produc.setLayoutManager(lineal);
        productos prod = new productos();
        productos2 his = new productos2();
        hista.setLayoutManager(lineal2);


        firestore.collection("usuarios").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> numeros = new ArrayList<>();
                ArrayList<String> hist = new ArrayList<>();
                hist = (ArrayList<String>) documentSnapshot.get("historial");
                numeros = (ArrayList<String>) documentSnapshot.get("trasporte");
                his.setUnicos(hist);
                hista.setAdapter(his);
                his.notifyDataSetChanged();



                prod.setUnicos(numeros);
                produc.setAdapter(prod);
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

        DatabaseReference refenrencia;

        @SuppressLint("MissingInflatedId")
        public void mostrar(int pos) {
            AlertDialog.Builder s = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View view = getLayoutInflater().inflate(R.layout.input_pedidos, null);
            TextView corr = view.findViewById(R.id.Correo_id);
            TextView id_ped = view.findViewById(R.id.Pedido_id);
            TextView tef_id = view.findViewById(R.id.telefono_id);
            TextView seg_id = view.findViewById(R.id.Seguimiento_id);
            TextView costo = view.findViewById(R.id.costo_id);
            TextView dire = view.findViewById(R.id.Dirrecion_id);
            Button cancelar = view.findViewById(R.id.cancelar_pedidos);
            Button entregar = view.findViewById(R.id.entregado);
            Button factura = view.findViewById(R.id.FACTURA);
            String titulo, desc, pago, nombre, id_p, dirrecion, correo, empresa;
            corr.setText(user.getEmail());
            firestore.collection("nuevos pedidos").document(unicos.get(pos)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    id_ped.setText(documentSnapshot.getString("id"));
                    tef_id.setText(documentSnapshot.getString("telefono"));
                    seg_id.setText(documentSnapshot.getString("segumiento"));
                    costo.setText(documentSnapshot.getString("pago") + "€");
                    dire.setText(documentSnapshot.getString("dirrecion"));
                }
            });
            entregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sumar_finalizado2(pos);
                    DocumentReference re = firestore.collection("usuarios").document(corr.getText().toString());
                    re.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> lista = new ArrayList<>();
                            lista = (ArrayList<String>) documentSnapshot.get("trasporte");
                            lista.remove(unicos.get(pos));
                            re.update("trasporte", lista).isSuccessful();
                        }
                    });



                    firestore.collection("nuevos pedidos").document(unicos.get(pos)).delete();
                    alerta = new alert("se ha entregado el pedido con exito");
                    alerta.show(getParentFragmentManager(), "alerta");
                }
            });


            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentReference ad = firestore.collection("nuevos pedidos").document(unicos.get(pos));
                    ad.update("segumiento", "Cancelado").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            sumar_finalizado();
                            alerta = new alert("pedido cancelado con exito");
                            alerta.show(getParentFragmentManager(), "alerta");
                        }
                    });


                }
            });
            factura.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPermission()) {
                        Toast.makeText(getContext(), "Permiso Aceptado", Toast.LENGTH_LONG).show();

                    } else {
                        requestPermissions();
                    }
                    generarPdf(unicos.get(pos));
                }
            });
            s.setView(view);
            s.show();
        }

        public void sumar_finalizado() {
            firestore.collection("pedidos").document("finalizados").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a = documentSnapshot.getLong("numero").intValue();

                    a++;

                    DocumentReference ad = firestore.collection("pedidos").document("finalizados");
                    ad.update("numero", a).isSuccessful();
                }
            });
            firestore.collection("pedidos").document("emision").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a = documentSnapshot.getLong("numero").intValue();

                    a--;

                    DocumentReference ad = firestore.collection("pedidos").document("emision");
                    ad.update("numero", a).isSuccessful();
                }
            });
            firestore.collection("pedidos").document("cancelados").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a = documentSnapshot.getLong("numero").intValue();

                    a++;

                    DocumentReference ad = firestore.collection("pedidos").document("cancelados");
                    ad.update("numero", a).isSuccessful();
                }
            });
        }

        public void sumar_finalizado2(int o) {
            firestore.collection("pedidos").document("finalizados").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a = documentSnapshot.getLong("numero").intValue();

                    a++;

                    DocumentReference ad = firestore.collection("pedidos").document("finalizados");
                    ad.update("numero", a).isSuccessful();
                }
            });
            firestore.collection("pedidos").document("emision").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int a = documentSnapshot.getLong("numero").intValue();

                    a--;

                    DocumentReference ad = firestore.collection("pedidos").document("emision");
                    ad.update("numero", a).isSuccessful();
                }
            });
            firestore.collection("pedidos").document("pedidos_admin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    ArrayList<String> lista = new ArrayList<>();
                    lista = (ArrayList<String>) documentSnapshot.get("pedidos");
                    lista.remove(unicos.get(o));
                    DocumentReference f = firestore.collection("pedidos").document("pedidos_admin");
                    f.update("pedidos", lista).isSuccessful();
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
                        p1.setText(documentSnapshot.getString("nombre"));
                        if (documentSnapshot.getString("segumiento").equals("Entregado") ) {
                            p3.setTextColor(Color.rgb(252, 3, 44));
                        } else {
                            p3.setTextColor(Color.rgb(0, 255, 34));
                        }
                        p3.setText(documentSnapshot.getString("segumiento"));
                        p2.setText(documentSnapshot.getString("pago") + "€");
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
            return new productos2.Adaptador(getLayoutInflater().inflate(R.layout.historial, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull productos2.Adaptador holder, int position) {
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
            System.out.println(unicos.size());
            return unicos.size();
        }


        public void mostrar(int pos) {

        }

        private class Adaptador extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView p1, p2, p3;


            public Adaptador(@NonNull View itemView) {
                super(itemView);
                p1 = itemView.findViewById(R.id.historial_producto);
                itemView.setOnClickListener(this);

            }

            public void imprimir(int position) {

                firestore.collection("usuarios").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        p1.setText(unicos.get(position));

                    }
                });

            }


            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());


            }
        }
    }


    public void generarPdf(String a) {
        firestore.collection("nuevos pedidos").document(a).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                TextPaint titulo = new TextPaint();
                TextPaint descripcion = new TextPaint();
                TextPaint precio = new TextPaint();
                TextPaint Direcion = new TextPaint();
                TextPaint num_id = new TextPaint();
                TextPaint Telefono = new TextPaint();
                TextPaint cabeza = new TextPaint();
                TextPaint importante = new TextPaint();
                String name = documentSnapshot.getString("nombre");
                String dir = documentSnapshot.getString("dirrecion");
                String pago = documentSnapshot.getString("pago");
                String corr = documentSnapshot.getString("correo");
                String id = documentSnapshot.getString("id");

                String comple = "Factura a Nombre de: \n " + name;
                String compledr = "La dirrecion de envio a la que se ha de enviar su paquete: \n" + dir;
                String compleid = "El id del pedido es: \n" + id + "#";
                String complepa = "Se ha pagado el total de:\n " + pago + "€";
                String telc = "El numero de telefono:\n " + documentSnapshot.getString("telefono");
                String importa = "Si tienes alguna duda de tu paquete llama a:" + 64126685 + "\n o escribe al correo jjmlj10@gmail.com para colocarnos en contacto";
                Bitmap bitmap, bitmapEscala;

                PdfDocument.PageInfo paginaInfo = new PdfDocument.PageInfo.Builder(1316, 754, 1).create();
                PdfDocument.Page pagina1 = pdfDocument.startPage(paginaInfo);

                Canvas canvas = pagina1.getCanvas();

                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bella);
                bitmapEscala = Bitmap.createScaledBitmap(bitmap, 400, 380, false);
                canvas.drawBitmap(bitmapEscala, 868, 10, paint);

                titulo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titulo.setTextSize(50);
                titulo.setColor(Color.rgb(255, 0, 17));
                canvas.drawText("Factura de Compra", 10, 300, titulo);


                cabeza.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                cabeza.setTextSize(20);
                canvas.drawText(comple, 10, 350, cabeza);

                Direcion.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                Direcion.setTextSize(20);
                canvas.drawText(compledr, 10, 400, Direcion);

                num_id.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                num_id.setTextSize(20);
                canvas.drawText(compleid, 10, 450, num_id);

                Telefono.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                Telefono.setTextSize(20);
                canvas.drawText(telc, 10, 500, Telefono);


                precio.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                precio.setTextSize(20);
                canvas.drawText(complepa, 10, 550, precio);


                descripcion.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                descripcion.setTextSize(16);
                descripcion.setColor(Color.rgb(255, 0, 17));
                canvas.drawText(importa, 10, 700, descripcion);


                precio.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                precio.setTextSize(24);
                canvas.drawText("Gracias por comprar en Profit vuelva pronto", 10, 650, precio);

                pdfDocument.finishPage(pagina1);

                File file = new File(Environment.getExternalStorageDirectory(), "Archivo.pdf");
                try {
                    pdfDocument.writeTo(new FileOutputStream(file));
                    Toast.makeText(getContext(), "Se creo el PDF correctamente", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pdfDocument.close();
            }
        });

    }

    private boolean checkPermission() {
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getContext(), "Permiso concedido", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Permiso denegado", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }
        }
    }
}