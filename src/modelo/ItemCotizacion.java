package modelo;

public class ItemCotizacion {
    private ItemCatalogo referencia;
    private int cantidad;
    private double precioUnitario;

    public ItemCotizacion(ItemCatalogo referencia, int cantidad) {
        this.referencia = referencia;
        this.cantidad = cantidad;
        this.precioUnitario = referencia.getPrecio(); // Congela el precio al momento de cotizar
    }

    public ItemCatalogo getReferencia() { return referencia; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }
}
