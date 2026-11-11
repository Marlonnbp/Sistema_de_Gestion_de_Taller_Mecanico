package modelo;

public class Repuesto extends ItemCatalogo {

    private int stock;

    public Repuesto(String codigo, String nombre, double precio, int stock) {
        super(codigo, nombre, precio);
        setStock(stock);
    }

    public int getStock() { return stock; }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public boolean hayStock(int cantidad) {
        return cantidad > 0 && stock >= cantidad;
    }

    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente de " + getNombre() + ".");
        }
        stock -= cantidad;
    }

    public void reponerStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        stock += cantidad;
    }

    @Override
    public String getTipo() {
        return "REPUESTO";
    }

    @Override
    public String toLinea() {
        return getTipo() + ";" + lineaBase() + ";" + stock;
    }

    public static Repuesto desdeLinea(String linea) {
        String[] p = linea.split(";");
        // Nuevo formato: TIPO(0); CODIGO(1); NOMBRE(2); PRECIO(3); ACTIVO(4); STOCK(5)
        if (p.length != 6 || !p[0].equals("REPUESTO")) {
            throw new IllegalArgumentException("Línea de repuesto inválida: " + linea);
        }
        Repuesto r = new Repuesto(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[5]));
        r.setActivo(Boolean.parseBoolean(p[4]));
        return r;
    }

    @Override
    public String toString() {
        return super.toString() + " | Stock: " + stock;
    }
}