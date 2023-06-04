package com.example.administrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vista_admin_inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vista_admin_inicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public vista_admin_inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment vista_admin_inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static vista_admin_inicio newInstance(String param1, String param2) {
        vista_admin_inicio fragment = new vista_admin_inicio();
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

         View a=inflater.inflate(R.layout.fragment_vista_admin_inicio, container, false);

         a.findViewById(R.id.imagen1).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                renplace(new estadistica());
             }
         });
         a.findViewById(R.id.imagen2).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            renplace(new lista_segumiento());
             }
         });
         a.findViewById(R.id.imagen3).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
        renplace(new configuraciones_admin());
             }
         });
         a.findViewById(R.id.imagen4).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
            renplace(new vista_productos());
             }
         });
        return a;
    }
    public void renplace(Fragment a){
        FragmentManager d=getParentFragmentManager();
        FragmentTransaction ad=d.beginTransaction();
        ad.replace(R.id.frame,a);
        ad.addToBackStack(null);
        ad.commit();
    }
}