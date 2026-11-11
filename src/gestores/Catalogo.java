package modelo;

public class Catalogo {

    private static final int CAPACIDAD = 100;

    private final ItemCatalogo[] items;
    private int cantidad;

    public Catalogo() {
        items = new ItemCatalogo[CAPACIDAD];
        cantidad = 0;
    }

    public int getCantidad() {
        return cantidad;
    }

    public boolean estaLleno() {
        return cantidad == CAPACIDAD;
    }

    public boolean existe(String codigo) {
        return leer(codigo) != null;
    }

    public boolean crear(ItemCatalogo item) {
        if (item == null || estaLleno() || existe(item.getCodigo())) {
            return false;
        }
        items[cantidad++] = item;
        return true;
    }

    public ItemCatalogo leer(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getCodigo().equals(codigo.trim())) {
                return items[i];
            }
        }
        return null;
    }

    public ItemCatalogo[] buscarPorNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return new ItemCatalogo[0];
        }
        String criterio = texto.trim().toLowerCase();
        int total = 0;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getNombre().toLowerCase().contains(criterio)) {
                total++;
            }
        }
        ItemCatalogo[] resultado = new ItemCatalogo[total];
        int k = 0;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getNombre().toLowerCase().contains(criterio)) {
                resultado[k++] = items[i];
            }
        }
        return resultado;
    }

    public ItemCatalogo[] listar() {
        ItemCatalogo[] copia = new ItemCatalogo[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = items[i];
        }
        return copia;
    }

    public boolean modificarPrecio(String codigo, double nuevoPrecio) {
        ItemCatalogo item = leer(codigo);
        if (item == null) {
            return false;
        }
        item.setPrecio(nuevoPrecio);
        return true;
    }

    public boolean modificarStock(String codigo, int nuevoStock) {
        ItemCatalogo item = leer(codigo);
        if (!(item instanceof Repuesto)) {
            return false;
        }
        ((Repuesto) item).setStock(nuevoStock);
        return true;
    }

    public boolean eliminar(String codigo) {
        if (codigo == null) {
            return false;
        }
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getCodigo().equals(codigo.trim())) {
                for (int j = i; j < cantidad - 1; j++) {
                    items[j] = items[j + 1];
                }
                items[--cantidad] = null;
                return true;
            }
        }
        return false;
    }

    public boolean cargarDesdeLinea(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return false;
        }
        String tipo = linea.split(";")[0];
        if (tipo.equals("REPUESTO")) {
            return crear(Repuesto.desdeLinea(linea));
        }
        if (tipo.equals("SERVICIO")) {
            return crear(Servicio.desdeLinea(linea));
        }
        throw new IllegalArgumentException("Tipo de ítem desconocido: " + tipo);
    }

    public String[] toLineas() {
        String[] lineas = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            lineas[i] = items[i].toLinea();
        }
        return lineas;
    }
}