package verificador;

import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;

public class verificar {
    String Nombre,Descripcion,precio;

    public verificar() {
    }
    public boolean verificador(EditText Nombre, EditText Descripcion, EditText precio, EditText Talla, ImageView nom, ImageView pre, ImageView des, ImageView ta){
        String opciones[] = {"XS", "S", "M", "L", "XL", "XXL"};
        boolean tallas=false;
        if (Nombre.getText().toString()==null||!Nombre.getText().toString().matches("^[a-zA-Z ]{8,18}$")){
            Nombre.setError("Error el formato de no es el deseado");
            nom.setVisibility(View.GONE);
            return false;
        }else {
            nom.setVisibility(View.VISIBLE);
        }
        if (Descripcion.getText().toString()==null||!Descripcion.getText().toString().matches("^[a-zA-Z ]{12,50}$")){
            Descripcion.setError("Campo vacio");
            des.setVisibility(View.GONE);
            return false;
        }else {
            des.setVisibility(View.VISIBLE);
        }
        if (precio.getText().toString()==null||!precio.getText().toString().matches("^[0-9]+(\\.[0-9]*)?$")){
            precio.setError("Error el formato de no es el deseado");
            pre.setVisibility(View.GONE);
            return false;
        }else {
            pre.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i <opciones.length ; i++) {
           if (Talla.getText().toString()!=null&&Talla.getText().toString().equals(opciones[i])){
                tallas=true;
                break;
           }
        }
        if (tallas==false){
            Talla.setError("No concuerda la talla");
            ta.setVisibility(View.GONE);
            return false;
        }else{
            ta.setVisibility(View.VISIBLE);
        }
        return true;
    }
    public boolean veri_ingreso(EditText usuario,EditText pass){
        if (usuario.getText().toString().isEmpty()){
            usuario.setError("campos vacios");
            return false;
        }
        if (pass.getText().toString().isEmpty()){
            pass.setError("campo vacio");
            return false;
        }
        return true;
    }
}
