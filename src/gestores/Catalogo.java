package gestores;

import modelo.ItemCatalogo;
import modelo.Repuesto;
import modelo.Servicio;

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
        if (item == null || estaLleno()) {
            return false;
        }
        
        // Evitar duplicados por código y permitir reactivación
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getCodigo().equals(item.getCodigo())) {
                if (!items[i].isActivo()) {
                    items[i] = item; // Sobrescribe y reactiva el ítem
                    return true;
                }
                return false; // Ya existe y está activo
            }
        }
        
        items[cantidad++] = item;
        return true;
    }

    public ItemCatalogo leer(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (int i = 0; i < cantidad; i++) {
            // Solo retorna el ítem si está activo
            if (items[i].isActivo() && items[i].getCodigo().equals(codigo.trim())) {
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
        
        // Primero contamos cuántos ítems activos coinciden
        int total = 0;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].isActivo() && items[i].getNombre().toLowerCase().contains(criterio)) {
                total++;
            }
        }
        
        // Luego creamos el arreglo con el tamaño exacto y lo llenamos
        ItemCatalogo[] resultado = new ItemCatalogo[total];
        int k = 0;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].isActivo() && items[i].getNombre().toLowerCase().contains(criterio)) {
                resultado[k++] = items[i];
            }
        }
        return resultado;
    }

    public ItemCatalogo[] listar() {
        // Contar activos para dimensionar el arreglo
        int totalActivos = 0;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].isActivo()) {
                totalActivos++;
            }
        }
        
        ItemCatalogo[] copia = new ItemCatalogo[totalActivos];
        int k = 0;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].isActivo()) {
                copia[k++] = items[i];
            }
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

    public boolean actualizar(String codigo, String nuevoNombre, double nuevoPrecio, int nuevoStock) {
        ItemCatalogo item = leer(codigo);
        if (item == null) {
            return false;
        }
        item.setNombre(nuevoNombre);
        item.setPrecio(nuevoPrecio);
        if (item instanceof Repuesto && nuevoStock >= 0) {
            ((Repuesto) item).setStock(nuevoStock);
        }
        return true;
    }

    // Reemplaza eliminar() por darDeBaja() para mantener el historial
    public boolean darDeBaja(String codigo) {
        if (codigo == null) {
            return false;
        }
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getCodigo().equals(codigo.trim()) && items[i].isActivo()) {
                items[i].setActivo(false); // Baja lógica
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
        // Al guardar en archivos, se exportan TODOS (activos e inactivos)
        // para que las órdenes del historial no pierdan su referencia al cargarse.
        String[] lineas = new String[cantidad];
        for (int i = 0; i < cantidad; i++) {
            lineas[i] = items[i].toLinea();
        }
        return lineas;
    }
    
    public ItemCatalogo leerCualquiera(String codigo) {
        if (codigo == null) return null;
        for (int i = 0; i < cantidad; i++) {
            if (items[i].getCodigo().equals(codigo.trim())) {
                return items[i];
            }
        }
        return null;
    }
}