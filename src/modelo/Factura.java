package modelo;

import java.util.Date;

public class Factura {
    private String numero;
    private Date fechaEmision;
    private double subtotal;
    private double igv;
    private double total;
    private OrdenTrabajo orden;

    public Factura(String numero) {
        this.numero = numero;
    }

    public String getNumero() { return numero; }
    public Date getFechaEmision() { return fechaEmision; }
    public double getSubtotal() { return subtotal; }
    public double getIgv() { return igv; }
    public double getTotal() { return total; }
    public OrdenTrabajo getOrden() { return orden; }

    public void emitir(OrdenTrabajo orden) {
        this.orden = orden;
        this.fechaEmision = new Date();
        this.subtotal = orden.calcularSubtotal();
        this.igv = orden.calcularIGV();
        this.total = orden.calcularTotal();
        orden.cambiarEstado(EstadoOrden.FACTURADA);
    }
}
