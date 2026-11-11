package modelo;

public class Servicio extends ItemCatalogo {

    public Servicio(String codigo, String nombre, double precio) {
        super(codigo, nombre, precio);
    }

    @Override
    public String getTipo() {
        return "SERVICIO";
    }

    @Override
    public String toLinea() {
        return getTipo() + ";" + lineaBase();
    }

    public static Servicio desdeLinea(String linea) {
        String[] p = linea.split(";");
        if (p.length != 4 || !p[0].equals("SERVICIO")) {
            throw new IllegalArgumentException("Línea de servicio inválida: " + linea);
        }
        return new Servicio(p[1], p[2], Double.parseDouble(p[3]));
    }
}