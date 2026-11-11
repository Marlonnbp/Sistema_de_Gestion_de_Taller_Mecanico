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
        // Nuevo formato: TIPO(0); CODIGO(1); NOMBRE(2); PRECIO(3); ACTIVO(4)
        if (p.length != 5 || !p[0].equals("SERVICIO")) {
            throw new IllegalArgumentException("Línea de servicio inválida: " + linea);
        }
        Servicio s = new Servicio(p[1], p[2], Double.parseDouble(p[3]));
        s.setActivo(Boolean.parseBoolean(p[4]));
        return s;
    }
}