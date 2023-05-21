package com.example.administrador;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link configuraciones_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class configuraciones_admin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth auth;
    FirebaseUser user;
    alert alerta;
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    ArrayList<String>admin=new ArrayList<>();

    public configuraciones_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment configuraciones_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static configuraciones_admin newInstance(String param1, String param2) {
        configuraciones_admin fragment = new configuraciones_admin();
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

         View a=inflater.inflate(R.layout.fragment_configuraciones_admin, container, false);
         TextView vista_correo=a.findViewById(R.id.vista_correo);

        TextView d=a.findViewById(R.id.conf);
        TextView change=a.findViewById(R.id.changepass);
        TextView e=a.findViewById(R.id.Cerrar);
        TextView f=a.findViewById(R.id.changecorreo);
        Button eliminar=a.findViewById(R.id.eliminar_cuenta);

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();


        vista_correo.setText(user.getEmail());
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                getActivity().finish();

            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo_antiguo=user.getEmail();
                consulta_firestore();
                AlertDialog.Builder s=new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View view =getLayoutInflater().inflate(R.layout.cambios_admin,null);
                EditText correo=view.findViewById(R.id.editracorreo);
                correo.setText(user.getEmail());
                s.setView(view);
                s.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.updateEmail(correo.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    ingresar_info(correo.getText().toString(),correo_antiguo);
                                    alerta=new alert("Correo exitosamente actualizado");
                                    auth.signOut();
                                    getActivity().finish();
                                }else{
                                    alerta=new alert("Error formato no deseado");
                                }
                                alerta.show(getParentFragmentManager(),"alerta");
                            }
                        });

                    }
                });
                s.show();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder s=new AlertDialog.Builder(getActivity());
                s.setTitle("cambio de contraseña");
                s.setMessage("estas seguro de cambiar su contraseña?");
                s.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alerta=new alert("Compruebe su correo para cambiar las contrase");
                        auth.sendPasswordResetEmail(user.getEmail());

                    }
                });
                s.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                s.show();
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nuevo_admin a=new nuevo_admin();
                a.setArguments(getArguments());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,a).addToBackStack(null).commit();
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo_eliminar=user.getEmail();
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                        eliminar(correo_eliminar);
                        alerta=new alert("usuario eliminado exitosamente");
                   }else{
                    alerta=new alert("no se ha podido eliminar su cuenta");
                   }
                   alerta.show(getParentFragmentManager(),"alerta");
                    }
                });
            }
        });

        return a;
    }
    public void ingresar_info(String gmail,String antiguo) {
        consulta_firestore();
        admin.remove(antiguo);
        admin.add(gmail);
        firestore.collection("administradores").document("usuarios").update("admin", admin);

    }
    public void eliminar(String gmail) {
        consulta_firestore();
        admin.remove(gmail);
        firestore.collection("administradores").document("usuarios").update("admin", admin);
    }
    public void consulta_firestore() {
        firestore.collection("administradores").document("usuarios").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    admin = (ArrayList<String>) documentSnapshot.get("admin");
                }
            }
        });
    }


}