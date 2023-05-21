package modelo;

public class pedidos {

    String estado;
    info_pedido pedido;

    public pedidos( String estado, info_pedido pedido) {

        this.estado = estado;
        this.pedido = pedido;
    }

    public pedidos() {
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public info_pedido getPedido() {
        return pedido;
    }

    public void setPedido(info_pedido pedido) {
        this.pedido = pedido;
    }
}
