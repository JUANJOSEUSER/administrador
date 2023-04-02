package modelo;

public class model_product {
    String Nombre,Descripcion,Talla;
    Float Precio;

    public model_product(String nombre, String descripcion, String talla, Float precio) {
        Nombre = nombre;
        Descripcion = descripcion;
        Talla = talla;
        Precio = precio;
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

    public Float getPrecio() {
        return Precio;
    }
}
