package modelo;

import java.util.Date;

public class OrdenTrabajo {
    private String codigo;
    private Date fechaIngreso;
    private String motivoIngreso;
    private EstadoOrden estado;
    private ItemCotizacion[] items;
    private int cantidadItems;
    private Cliente cliente;
    private Vehiculo vehiculo;

    public OrdenTrabajo(String codigo, String motivoIngreso, Cliente cliente, Vehiculo vehiculo) {
        this.codigo = codigo;
        this.fechaIngreso = new Date();
        this.motivoIngreso = motivoIngreso;
        this.estado = EstadoOrden.EN_ESPERA;
        this.items = new ItemCotizacion[50]; // Capacidad fija al no usar List
        this.cantidadItems = 0;
        this.cliente = cliente;
        this.vehiculo = vehiculo;
    }

    public String getCodigo() { return codigo; }
    public Date getFechaIngreso() { return fechaIngreso; }
    public String getMotivoIngreso() { return motivoIngreso; }
    public EstadoOrden getEstado() { return estado; }
    public ItemCotizacion[] getItems() { return items; }
    public int getCantidadItems() { return cantidadItems; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }

    public void setMotivoIngreso(String motivoIngreso) { this.motivoIngreso = motivoIngreso; }

    public void agregarItem(ItemCatalogo item, int cantidad) {
        if (cantidadItems < items.length) {
            items[cantidadItems] = new ItemCotizacion(item, cantidad);
            cantidadItems++;
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < cantidadItems; i++) {
            subtotal += items[i].calcularSubtotal();
        }
        return subtotal;
    }

    public double calcularIGV() {
        return calcularSubtotal() * 0.18; // IGV 18% para Perú
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIGV();
    }

    public void cambiarEstado(EstadoOrden nuevo) {
        this.estado = nuevo;
    }

    public void aprobar() {
        this.estado = EstadoOrden.APROBADA;
    }

    public void anular() {
        this.estado = EstadoOrden.ANULADA;
    }

    public boolean estaActiva() {
        return this.estado != EstadoOrden.FACTURADA && this.estado != EstadoOrden.ANULADA;
    }
}
