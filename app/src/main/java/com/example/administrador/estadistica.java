package com.example.administrador;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link estadistica#newInstance} factory method to
 * create an instance of this fragment.
 */
public class estadistica extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore firestore;
    public estadistica() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment estadistica.
     */
    // TODO: Rename and change types and number of parameters
    public static estadistica newInstance(String param1, String param2) {
        estadistica fragment = new estadistica();
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
    String titulo[]={"cancelados","emision","finalizados","total"};
    String titulo2[]={"usuarios actuales","usuarios nuevos","usuarios de baja"};
    int ventas[]={10,30};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View a= inflater.inflate(R.layout.fragment_estadistica, container, false);
        firestore=FirebaseFirestore.getInstance();



try {
    BarChart line= a.findViewById(R.id.gráfico2);
    ArrayList<BarEntry> pie2=new ArrayList<>();


    firestore.collection("pedidos").document("usuarios nuevos").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {


            pie2.add(new BarEntry(1,documentSnapshot.getLong("numero"),"datos"));


            firestore.collection("pedidos").document("usuarios actuales").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    pie2.add(new BarEntry(2,documentSnapshot.getLong("numero"),"datso2"));
                }
            });
            firestore.collection("pedidos").document("usuarios de baja").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    pie2.add(new BarEntry(3,documentSnapshot.getLong("numero"),"datos3"));
                    BarDataSet da=new BarDataSet(pie2 ,"usuarios");
                    da.setColors(ColorTemplate.JOYFUL_COLORS);
                    BarData add=new BarData(da);
                    ArrayList<String > label=new ArrayList<>();
                    label.add("pepe");
                    label.add("usuarios nuevos");
                    label.add("usuarios actuales");
                    label.add("usuarios de baja");
                    XAxis adf=line.getXAxis();
                    adf.setValueFormatter(new IndexAxisValueFormatter(label));
                    adf.setLabelCount(label.size());
                    adf.setPosition(XAxis.XAxisPosition.TOP);
                    adf.setDrawGridLines(false);
                    adf.setDrawAxisLine(false);
                    adf.setGranularity(1f);
                    line.setData(add);
                    line.invalidate();
                }
            });


//


        }
    });







    PieChart chart=(PieChart) a.findViewById(R.id.gráfico);

    Description de=new Description();
    de.setText("Estado de pedidos");
    de.setTextSize(10f);
    chart.setDescription(de);


    ArrayList<PieEntry> pie=new ArrayList<>();
    for (int i = 0; i <titulo.length ; i++) {
        int finalI = i;
        firestore.collection("pedidos").document(titulo[i]).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pie.add(new PieEntry(documentSnapshot.getLong("numero").intValue(),titulo[finalI]));
                PieDataSet data= new PieDataSet(pie,"ventas");
                data.setSliceSpace(3f);

                data.setValueTextSize(20f);
                data.setSelectionShift(1f);
                data.setValueLineWidth(180f);
                data.setColors(ColorTemplate.JOYFUL_COLORS);
                data.setFormSize(20f);


                PieData ad=new PieData( data);
                chart.setHoleRadius(25f);
                chart.setUsePercentValues(true);

                chart.setTransparentCircleRadius(0);
                chart.setEntryLabelColor(R.color.black);
                chart.setEntryLabelTextSize(15f);
                chart.setData(ad);
            }
        });
    }
    chart.invalidate();
    chart.animateY(1000, Easing.EaseInOutCubic);

}catch (ArrayIndexOutOfBoundsException e){
    System.out.println("error");
}


        return a;
    }
}