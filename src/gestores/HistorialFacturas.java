package gestores;

import modelo.Factura;

public class HistorialFacturas {
    private Factura[] facturas;
    private int cantidad;

    public HistorialFacturas(int capacidadMaxima) {
        this.facturas = new Factura[capacidadMaxima];
        this.cantidad = 0;
    }

    public boolean crear(Factura f) {
        if (cantidad >= facturas.length) return false;
        
        facturas[cantidad] = f;
        cantidad++;
        return true;
    }

    public Factura[] leer(String criterio) {
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (facturas[i].getNumero().equalsIgnoreCase(criterio) ||
                facturas[i].getOrden().getCliente().getDni().equals(criterio)) {
                coincidencias++;
            }
        }

        Factura[] resultado = new Factura[coincidencias];
        int indice = 0;
        for (int i = 0; i < cantidad; i++) {
            if (facturas[i].getNumero().equalsIgnoreCase(criterio) ||
                facturas[i].getOrden().getCliente().getDni().equals(criterio)) {
                resultado[indice++] = facturas[i];
            }
        }
        return resultado;
    }
    
    public Factura[] listarTodos() {
        Factura[] copia = new Factura[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = facturas[i];
        }
        return copia;
    }

    public Factura[] listar() {
        return listarTodos();
    }

    public void agregarDesdeArchivo(Factura f) {
        if (cantidad < facturas.length) {
            facturas[cantidad++] = f;
        }
    }
}