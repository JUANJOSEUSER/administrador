package com.example.administrador;

import static android.app.Activity.RESULT_OK;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link perfil_usuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class perfil_usuario extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
FirebaseAuth firebaseAuth;
FirebaseUser user;
FirebaseFirestore firestore;
alert alerta;
    ImageView image;
    private static final int CATEGORY_APP_GALLERY = 1;
    StorageReference storage;
    public perfil_usuario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment perfil_usuario.
     */
    // TODO: Rename and change types and number of parameters
    public static perfil_usuario newInstance(String param1, String param2) {
        perfil_usuario fragment = new perfil_usuario();
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
        View a=inflater.inflate(R.layout.fragment_perfil_usuario, container, false);
        Button salir=a.findViewById(R.id.cerrar_sesion_usuario);
        Button cambio=a.findViewById(R.id.eliminar_cuenta_usuario);
        Button cambiopass=a.findViewById(R.id.cambio_contraseña_usuario);
        Button cambiocorreo=a.findViewById(R.id.cambio_correo_usuario);
        Button cuenta=a.findViewById(R.id.mi_cuenta);
        TextView nom=a.findViewById(R.id.nombre_perfil_usuario);
      image=a.findViewById(R.id.imagen_usuario);
        storage = FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        nom.setText(user.getEmail());
        firestore=FirebaseFirestore.getInstance();
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setMessage("Cerrar sesion");
                alert.setTitle("se va a cerrar la sesion");
                alert.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        getActivity().finish();
                    }
                });
                alert.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        });
        cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(getActivity(),mi_cuenta.class);
                startActivity(a);
            }
        });
        cambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
                alert.setMessage("se va a eliminar su cuenta");
                alert.setTitle("Eliminar cuenta");
                alert.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String gmail=user.getEmail();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    firestore.collection("usuarios").document(gmail).delete();
                                    storage.child("usuarios").child(gmail).delete();
                                    sumar_finalizado();
                                    alerta=new alert("se ha eliminado su cuenta correctamente");
                                    getActivity().finish();
                                }else{
                                    alerta=new alert("no se ha elimiando la cuenta");
                                }
                                alerta.show(getParentFragmentManager(),"alerta");
                            }
                        });
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
alert.show();
            }
        });
        cambiocorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    alerta=new alert("Correo exitosamente actualizado");
                                    firebaseAuth.signOut();
                                    getActivity().finish();
                                }else{
                                    alerta=new alert("Error formato no deseado");
                                }
                                alerta.show(getParentFragmentManager(),"alerta");
                            }
                        });

                    }
                });
                s.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                s.show();
            }
        });
        cambiopass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder s=new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View view =getLayoutInflater().inflate(R.layout.cambios_admin,null);
                EditText correo=view.findViewById(R.id.editracorreo);
                TextView cambio=view.findViewById(R.id.textocambio);
                cambio.setText("Cambi de contraseña");
                correo.setHint("Contraseña");
                s.setView(view);
                s.setPositiveButton("aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user.updatePassword(correo.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    alerta=new alert("la contrasela se ha cambiado");
                                    firebaseAuth.signOut();
                                    getActivity().finish();
                                }else{
                                    alerta=new alert("Error formato no deseado");
                                }
                                alerta.show(getParentFragmentManager(),"alerta");
                            }
                        });

                    }
                });
                s.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                s.show();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img = new Intent(Intent.ACTION_PICK);
                img.setType("image/*");
                startActivityForResult(img, CATEGORY_APP_GALLERY);
            }
        });
leer(user.getEmail(),image);

a.findViewById(R.id.pedidos).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        pedidos vista=new pedidos();

        vista.setArguments(getArguments());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmento,vista).addToBackStack(null).commit();
    }
});
        return a;
    }
    public void leer(String a, ImageView v) {
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imge = storageReference.child("usuarios/" + a);
        imge.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                v.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

    }
    public void sumar_finalizado(){
        firestore.collection("pedidos").document("usuarios de baja").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int a= documentSnapshot.getLong("numero").intValue();
                a++;

                DocumentReference ad= firestore.collection("pedidos").document("usuarios de baja");
                ad.update("numero",a).isSuccessful();
            }
        });
        firestore.collection("pedidos").document("usuarios actuales").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int a= documentSnapshot.getLong("numero").intValue();
                a--;

                DocumentReference ad= firestore.collection("pedidos").document("usuarios actuales");
                ad.update("numero",a).isSuccessful();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_APP_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            StorageReference file = storage.child("usuarios/" + user.getEmail());
            file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    image.setImageURI(uri);

                }
            });
        }
    }
}