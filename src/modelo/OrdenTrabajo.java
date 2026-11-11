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

    public boolean tieneStockDisponibleParaAprobar() {
        for (int i = 0; i < cantidadItems; i++) {
            if (items[i].getReferencia() instanceof Repuesto) {
                Repuesto r = (Repuesto) items[i].getReferencia();
                if (!r.hayStock(items[i].getCantidad())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void descontarStockRepuestos() {
        for (int i = 0; i < cantidadItems; i++) {
            if (items[i].getReferencia() instanceof Repuesto) {
                Repuesto r = (Repuesto) items[i].getReferencia();
                r.descontarStock(items[i].getCantidad());
            }
        }
    }

    public void reponerStockRepuestos() {
        for (int i = 0; i < cantidadItems; i++) {
            if (items[i].getReferencia() instanceof Repuesto) {
                Repuesto r = (Repuesto) items[i].getReferencia();
                r.reponerStock(items[i].getCantidad());
            }
        }
    }

    public void cambiarEstado(EstadoOrden nuevo) {
        if (nuevo == EstadoOrden.APROBADA) {
            aprobar();
            return;
        }
        if (nuevo == EstadoOrden.ANULADA) {
            anular();
            return;
        }
        this.estado = nuevo;
    }

    public void aprobar() {
        // Al aprobar la orden, se retiran físicamente los repuestos del almacén
        if (this.estado != EstadoOrden.APROBADA && this.estado != EstadoOrden.LISTA_PARA_ENTREGA && this.estado != EstadoOrden.FACTURADA) {
            descontarStockRepuestos();
        }
        this.estado = EstadoOrden.APROBADA;
    }

    public void anular() {
        // Si ya había sido aprobada o estaba lista, se reponen los repuestos de vuelta al almacén
        if (this.estado == EstadoOrden.APROBADA || this.estado == EstadoOrden.LISTA_PARA_ENTREGA) {
            reponerStockRepuestos();
        }
        this.estado = EstadoOrden.ANULADA;
    }

    public boolean estaActiva() {
        return this.estado != EstadoOrden.FACTURADA && this.estado != EstadoOrden.ANULADA;
    }
    
    public void setFechaIngreso(java.util.Date fecha) { 
        this.fechaIngreso = fecha; 
    }

    public void cargarItemHistorico(ItemCatalogo item, int cant, double precioHistorico) {
        if (cantidadItems < items.length) {
            ItemCotizacion ic = new ItemCotizacion(item, cant);
            ic.setPrecioUnitario(precioHistorico); // Mantiene el precio antiguo intacto
            items[cantidadItems++] = ic;
        }
    }
}
