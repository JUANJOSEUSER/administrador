package com.example.administrador;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link inicio_usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inicio_usuario extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    VideoView video;

    public inicio_usuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inicio_usuario.
     */
    // TODO: Rename and change types and number of parameters
    public static inicio_usuario newInstance(String param1, String param2) {
        inicio_usuario fragment = new inicio_usuario();
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
         View a=inflater.inflate(R.layout.fragment_inicio_usuario, container, false);
         video=a.findViewById(R.id.videoView2);
         video.setVideoURI(Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.pexels2));
         video.start();
         a.findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Uri link=Uri.parse("https://www.instagram.com/laurahernandez_01/");
                 Intent a=new Intent(Intent.ACTION_VIEW,link);
                 startActivity(a);
             }
         });
         a.findViewById(R.id.whatss).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Uri link=Uri.parse("https://api.whatsapp.com/send?phone=%2B34+641266859&text=Bienvenido+a+Profit+en+que+podemos+ayudarte");
                 Intent a=new Intent(Intent.ACTION_VIEW,link);
                 startActivity(a);
             }
         });
         a.findViewById(R.id.Correo).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent a = new Intent(Intent.ACTION_SEND);
                 a.putExtra(Intent.EXTRA_EMAIL, new String[]{"jjmlj10@gmail.com"});
                 a.putExtra(Intent.EXTRA_SUBJECT, "Problema o inconveniente");
                 a.putExtra(Intent.EXTRA_TEXT, "descripcion del problema");
                 a.setType("message/rfc822");
                 startActivity(a);
             }
         });
         a.findViewById(R.id.comprar).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 renplace(new productos_usuarios());
             }
         });
        return a;
    }
    public void renplace(Fragment a){
        FragmentManager d=getParentFragmentManager();
        FragmentTransaction ad=d.beginTransaction();
        ad.replace(R.id.fragmento,a);
        ad.addToBackStack(null);
        ad.commit();
    }
}