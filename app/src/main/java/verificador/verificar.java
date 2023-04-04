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
            return false;
        }else {
            nom.setVisibility(View.VISIBLE);
        }
        if (Descripcion.getText().toString()==null){
            Descripcion.setError("Campo vacio");
            return false;
        }else {
            des.setVisibility(View.VISIBLE);
        }
        if (precio.getText().toString()==null||!precio.getText().toString().matches("^[0-9]+(\\.[0-9]*)?$")){
            precio.setError("Error el formato de no es el deseado");
            return false;
        }else {
            pre.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i <opciones.length ; i++) {
           if (Talla.getText().toString()!=null||Talla.getText().toString().equals(opciones[i])){
                tallas=true;
           }
        }
        if (tallas==false){
            Talla.setError("No concuerda la talla");
            return false;
        }else{
            ta.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
