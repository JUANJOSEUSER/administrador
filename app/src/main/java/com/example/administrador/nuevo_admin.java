package com.example.administrador;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import verificador.verificar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link nuevo_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nuevo_admin extends Fragment {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    alert alertas;
    FirebaseUser user;
    ArrayList<String> admin = new ArrayList<>();
    verificar vef=new verificar();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public nuevo_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nuevo_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static nuevo_admin newInstance(String param1, String param2) {
        nuevo_admin fragment = new nuevo_admin();
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

    EditText gmail, pass, conf_pass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View a = inflater.inflate(R.layout.fragment_nuevo_admin, container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user=auth.getCurrentUser();
        gmail = a.findViewById(R.id.admin_gmail);
        pass = a.findViewById(R.id.admin_password);
        conf_pass = a.findViewById(R.id.conf_admin_password);
        a.findViewById(R.id.volver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        a.findViewById(R.id.aceptar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (vef.verificacion_crear_cuenta_admin(gmail,pass,conf_pass)){
                    auth.createUserWithEmailAndPassword(gmail.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ingresar_info();
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            alertas = new alert("se ha creado la cuenta perfectamente porfavor verificar el correo ");
                                            alertas.show(getParentFragmentManager(),"alerta");
                                        }
                                    }
                                });


                            } else {
                                alertas = new alert("no se ha creado la cuenta");
                                alertas.show(getParentFragmentManager(),"alerta");
                            }
                        }
                    });
                }




//

//
            }
        });
        return a;

    }

    public void ingresar_info() {
        consulta_firestore();
        admin.add(gmail.getText().toString());
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