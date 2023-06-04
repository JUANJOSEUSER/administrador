package com.example.administrador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.info_pedido;
import modelo.usuario;

public class pagos extends AppCompatActivity {
    int paypalcode=7171;
    alert alerta;
    TextView total,telefono,dirrecion,nombre;
    int total_guardado=0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    usuario us=new usuario();
    int num=0,num2=0;
    String producto;
    int modo=2;
    public static String clave="AZaouBk2jcTFbDk-ajw-IwusjijC9aBwQibg7iJ_03M-j2CDvADQHCnIGP9JV5JpS-jl2Ju6vO1zGlbT";

    PayPalConfiguration config=new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(clave);
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        Bundle parametros = this.getIntent().getExtras();
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        total=findViewById(R.id.total);
        total_guardado=parametros.getInt("precio");
        modo=parametros.getInt("modo");
        producto=parametros.getString("producto");
        total.setText(String.valueOf(total_guardado+"€"));
        telefono=findViewById(R.id.telefono_total);
        dirrecion=findViewById(R.id.dirrecion);
        nombre=findViewById(R.id.nombre_pago);
        DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                us=documentSnapshot.toObject(usuario.class);
                nombre.setText(us.getNombre());
                telefono.setText(us.getTelefono());
                dirrecion.setText(us.getDirrecion());

            }
        });
        firestore.collection("pedidos").document("total").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                num=documentSnapshot.getLong("numero").intValue();
            }
        });
        firestore.collection("pedidos").document("emision").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                num2=documentSnapshot.getLong("numero").intValue();
            }
        });
        Intent se=new Intent(this, PayPalService.class);
        se.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(se);
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }
    public void pagar(View view) {

        if (!nombre.getText().toString().equals("")&&!telefono.getText().toString().equals("")&&!dirrecion.getText().toString().equals("")){
            PayPalPayment pay=new PayPalPayment(new BigDecimal(total_guardado),"EUR","pago a Realizar",PayPalPayment.PAYMENT_INTENT_SALE);
            Intent a=new Intent(this, PaymentActivity.class);
            a.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
            a.putExtra(PaymentActivity.EXTRA_PAYMENT,pay);
            startActivityForResult(a,paypalcode);
        }else{
            alerta=new alert("Falta por colocar la dirrecion de envio");
            alerta.show(getSupportFragmentManager(),"alerta");
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==paypalcode){
            if (resultCode==RESULT_OK){
                PaymentConfirmation confirmation=data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation!=null){
                    try {
                        String payme=confirmation.toJSONObject().toString(4);
                        DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
                        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                us=documentSnapshot.toObject(usuario.class);
                                ArrayList<info_pedido> addf=new ArrayList<>();
                                num=num+1;
                                num2=num2+1;
                                DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
                                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        us=documentSnapshot.toObject(usuario.class);
                                        info_pedido inf=null;
                                        if (modo==1){
                                            ArrayList<String >pro=new ArrayList<>();
                                            pro.add(producto);
                                            inf=new info_pedido(String.valueOf(num),us.getNombre(),us.getTelefono(),us.getDirrecion(),String.valueOf(total_guardado),user.getEmail(),"En proceso",pro);
                                        }else{
                                           inf=new info_pedido(String.valueOf(num),us.getNombre(),us.getTelefono(),us.getDirrecion(),String.valueOf(total_guardado),user.getEmail(),"En proceso",us.getProductos());
                                        }

                                        firestore.collection("nuevos pedidos").document(String.valueOf(num)).set(inf);


                                    }
                                });
                                pedidos_admin(num);
                                DocumentReference s=firestore.collection("pedidos").document("total");
                                DocumentReference df=firestore.collection("pedidos").document("emision");
                                df.update("numero",num2).isSuccessful();
                                s.update("numero",num).isSuccessful();
                                limpiar();


                            }
                        });

                    }catch (JSONException e){

                    }
                }
            }else if(resultCode==RESULT_CANCELED){
                alerta =new alert("Error no se a procesado su pago");
            } else if (resultCode==PaymentActivity.RESULT_EXTRAS_INVALID) {
                alerta=new alert("Error formato no valido");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void limpiar(){
        DocumentReference ref= firestore.collection("usuarios").document(user.getEmail());
        ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                us=documentSnapshot.toObject(usuario.class);
if (modo==1){
    ArrayList<String >pre=new ArrayList<>();
    pre.add(comienzo());
    us.getHistorial().addAll(pre);
    us.getTrasporte().add(String.valueOf(num));
}else{
    us.getTrasporte().add(String.valueOf(num));
    us.getHistorial().addAll(us.getProductos());
    us.getProductos().clear();
}
                firestore.collection("usuarios").document(user.getEmail()).set(us);
mandar_datos();
finish();



            }
        });
    }
    public void pedidos_admin(int a){
        firestore.collection("pedidos").document("pedidos_admin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            ArrayList<String>lista=new ArrayList<>();
            lista= (ArrayList<String>) documentSnapshot.get("pedidos");
            lista.add(String.valueOf(a));
            DocumentReference R=firestore.collection("pedidos").document("pedidos_admin");
            R.update("pedidos",lista).isSuccessful();
            }
        });
    }
    public String comienzo(){
        SharedPreferences librito= getApplication().getSharedPreferences("intermediador", Context.MODE_PRIVATE);
        return librito.getString("producto","");
    }
    public void mandar_datos(){//cada vez que se inicia seccion se crea un xml donde guardaremos datos en memoria
        SharedPreferences librito= getApplication().getSharedPreferences("intermediador", Context.MODE_PRIVATE);//se coloca el nombre del xml y el context si quiere ser privado o de acceso restringido
        SharedPreferences.Editor libro=librito.edit();//editor hace la funcion de poder escribir en el xml mandadole la clave y el valor
        libro.putBoolean("felicidades",true);

        libro.commit();
    }
}