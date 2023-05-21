package verificador;

import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.type.PostalAddress;

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
        if (Descripcion.getText().toString()==null||!Descripcion.getText().toString().matches("^[a-zA-Z ]{12,200}$")){
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

    public boolean verificacion_crear_cuenta(EditText Nombre, EditText gmail, EditText telefono,EditText password,EditText password2){
     if (!Nombre.getText().toString().matches("^[a-zA-Z]{3,12}$")){
         Nombre.setError("Error el Nombre no cumple  las restricciones");
        return false;
     }
     if (!gmail.getText().toString().matches("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+")){
         gmail.setError("Error en el gmail no es el formato deseado");
         return false;

     }
        if (!telefono.getText().toString().matches("^\\d{9}$")){
            telefono.setError("Error el telefono no es valido");
            return false;
        }
        if (password.getText().toString().matches("^(?=.*\\d)[a-zA-Z\\d]{6,12}$")){
            password.setError("Error la contraseña no cumple la restriccion");
            return false;
        }
        if (!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Error son iguales ");
        return false;
        }
        return true;
    }
    public boolean verificacion_crear_cuenta_admin(EditText gmail,EditText password,EditText password2){

        if (!gmail.getText().toString().matches("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+")&&gmail.getText().toString().isEmpty()){
            gmail.setError("Error en el gmail no es el formato deseado");
            return false;

        }

        if (!password.getText().toString().matches("^(?=.*\\d)[a-zA-Z\\d]{6,12}$")&&password.getText().toString().isEmpty()){
            password.setError("Error la contraseña no cumple la restriccion");
            return false;
        }
        if (!password.getText().toString().equals(password2.getText().toString())){
            password2.setError("Error son iguales ");
            return false;
        }
        if (password2.getText().toString().isEmpty()){
            password2.setError("Error campo vacio");
            return false;
        }
        return true;
    }
    public boolean verificar_cambio_cuenta(EditText nombre, EditText telefono, EditText dirrecion, EditText postal, EditText ciudad){
if (!nombre.getText().toString().matches("^[a-zA-Z ]{8,18}$")){
    nombre.setError("Error el nombre no cumple las restricciones");
    return false;
}
if (!telefono.getText().toString().matches("^\\d{9}$")){
    telefono.setError("Error el telefono debe de tener 9 digitos");
    return false;

}

if (!postal.getText().toString().matches("^\\d{5}$")){
    postal.setError("Error el codigo postal debe tener 5 digitos");
    return false;
}
return true;
    }
}
