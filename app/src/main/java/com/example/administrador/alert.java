package com.example.administrador;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class alert extends DialogFragment {
String modo;
Context pantalla;

public alert(String modo){
    modo=modo;
}



    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("INFORMACION");
        alert.setMessage(modo);

        return alert.create();

    }
}
