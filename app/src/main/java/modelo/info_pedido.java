package modelo;

import java.util.ArrayList;

public class info_pedido  {
    String nombre,telefono,dirrecion,pago,correo,segumiento,id;
    ArrayList<String>productos;


    public info_pedido(String id,String nombre, String telefono, String dirrecion, String pago, String correo, String segumiento,ArrayList<String>product) {
        this.id=id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.dirrecion = dirrecion;
        this.pago = pago;
        this.correo = correo;
        this.segumiento = segumiento;
        this.productos=product;

    }

    public ArrayList<String> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<String> productos) {
        this.productos = productos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public info_pedido() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDirrecion() {
        return dirrecion;
    }

    public void setDirrecion(String dirrecion) {
        this.dirrecion = dirrecion;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSegumiento() {
        return segumiento;
    }

    public void setSegumiento(String segumiento) {
        this.segumiento = segumiento;
    }
}
