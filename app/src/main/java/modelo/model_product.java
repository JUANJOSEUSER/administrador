package modelo;

public class model_product {
    String Nombre,Descripcion,Talla;
    String Precio;

    public model_product(String nombre, String descripcion, String talla, String precio) {
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

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getTalla() {
        return Talla;
    }

    public void setTalla(String talla) {
        Talla = talla;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }
}
