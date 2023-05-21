package com.example.administrador;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import modelo.usuario;

public class pagos extends AppCompatActivity {
    int paypalcode=7171;
    TextView total,telefono,dirrecion,nombre;
    int total_guardado=0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    usuario us=new usuario();
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
            PayPalPayment pay=new PayPalPayment(new BigDecimal("10"),"EUR","padado por mi",PayPalPayment.PAYMENT_INTENT_SALE);
            Intent a=new Intent(this, PaymentActivity.class);
            a.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
            a.putExtra(PaymentActivity.EXTRA_PAYMENT,pay);
            startActivityForResult(a,paypalcode);
        }else{
            System.out.println("falta");
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
                        System.out.println("esta hecho");
                    }catch (JSONException e){

                    }
                }
            }else if(resultCode==RESULT_CANCELED){
                System.out.println("cancelado");
            } else if (resultCode==PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out.println("invalido");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}