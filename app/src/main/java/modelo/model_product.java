package modelo;

public class model_product {
    String Nombre,Descripcion,Talla;
    String Precio;

    public model_product(String nombre, String descripcion, String talla, String  precio) {
        Nombre = nombre;
        Descripcion = descripcion;
        Talla = talla;
        Precio = precio;
    }

    public model_product() {

    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public String getTalla() {
        return Talla;
    }

    public String  getPrecio() {
        return Precio;
    }
}
