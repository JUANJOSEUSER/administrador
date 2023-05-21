package modelo;

import java.util.ArrayList;

public class usuario {
    String Telefono,nombre,Dirrecion;
    ArrayList<String>productos;
    String trasporte;
    ArrayList<String>historial;


    public usuario(String telefono, String nombre, ArrayList<String> productos, String trasporte, ArrayList<String> historial,String direcion) {
        Telefono = telefono;
        this.nombre = nombre;
        this.productos = productos;
        this.trasporte = trasporte;
        this.historial = historial;
        this.Dirrecion=direcion;
    }

    public String getDirrecion() {
        return Dirrecion;
    }

    public void setDirrecion(String dirrecion) {
        Dirrecion = dirrecion;
    }

    public ArrayList<String> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<String> productos) {
        this.productos = productos;
    }

    public String getTrasporte() {
        return trasporte;
    }

    public void setTrasporte(String trasporte) {
        this.trasporte = trasporte;
    }

    public ArrayList<String> getHistorial() {
        return historial;
    }

    public void setHistorial(ArrayList<String> historial) {
        this.historial = historial;
    }

    public usuario() {
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
