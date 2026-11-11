package modelo;

public abstract class ItemCatalogo {

    private final String codigo;
    private String nombre;
    private double precio;

    public ItemCatalogo(String codigo, String nombre, double precio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        this.codigo = codigo.trim();
        setNombre(nombre);
        setPrecio(precio);
    }

    // ---------- Getters ----------

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }



    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre.trim();
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }




    public double calcularImporte(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        return precio * cantidad;
    }


    public abstract String getTipo();

    public abstract String toLinea();

    protected String lineaBase() {
        return codigo + ";" + nombre + ";" + precio;
    }

    // ---------- Object ----------


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ItemCatalogo)) return false;
        ItemCatalogo otro = (ItemCatalogo) obj;
        return codigo.equals(otro.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return getTipo() + " [" + codigo + "] " + nombre + " - S/ " + String.format("%.2f", precio);
    }
}