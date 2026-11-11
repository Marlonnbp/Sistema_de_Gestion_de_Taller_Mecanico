package gestores;

import modelo.Cliente;
import modelo.OrdenTrabajo;
import modelo.Vehiculo;
import modelo.EstadoOrden;

public class GestorOrdenes {
    private OrdenTrabajo[] ordenes;
    private int cantidad;

    public GestorOrdenes(int capacidadMaxima) {
        this.ordenes = new OrdenTrabajo[capacidadMaxima];
        this.cantidad = 0;
    }

    public OrdenTrabajo crear(Cliente c, Vehiculo v, String motivo) {
        if (cantidad >= ordenes.length) return null;
        
        // Generador simple de código
        String codigo = "ORD-" + String.format("%04d", cantidad + 1);
        OrdenTrabajo nueva = new OrdenTrabajo(codigo, motivo, c, v);
        
        ordenes[cantidad] = nueva;
        cantidad++;
        return nueva;
    }

    public OrdenTrabajo[] leer(String criterio) {
        // Cuenta coincidencias para dimensionar el arreglo de retorno
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (ordenes[i].getCodigo().equalsIgnoreCase(criterio) || 
                ordenes[i].getCliente().getDni().equals(criterio) ||
                ordenes[i].getVehiculo().getPlaca().equalsIgnoreCase(criterio)) {
                coincidencias++;
            }
        }
        
        OrdenTrabajo[] resultado = new OrdenTrabajo[coincidencias];
        int indice = 0;
        for (int i = 0; i < cantidad; i++) {
            if (ordenes[i].getCodigo().equalsIgnoreCase(criterio) || 
                ordenes[i].getCliente().getDni().equals(criterio) ||
                ordenes[i].getVehiculo().getPlaca().equalsIgnoreCase(criterio)) {
                resultado[indice++] = ordenes[i];
            }
        }
        return resultado;
    }

    public boolean actualizar(OrdenTrabajo o) {
        for (int i = 0; i < cantidad; i++) {
            if (ordenes[i].getCodigo().equalsIgnoreCase(o.getCodigo())) {
                ordenes[i] = o;
                return true;
            }
        }
        return false;
    }

    public boolean anular(String codigo) {
        for (int i = 0; i < cantidad; i++) {
            if (ordenes[i].getCodigo().equalsIgnoreCase(codigo)) {
                if (ordenes[i].estaActiva()) {
                    ordenes[i].anular();
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean tieneOrdenesActivas(Cliente c) {
        for (int i = 0; i < cantidad; i++) {
            if (ordenes[i].getCliente().getDni().equals(c.getDni()) && ordenes[i].estaActiva()) {
                return true;
            }
        }
        return false;
    }

    public boolean tieneOrdenActiva(Vehiculo v) {
        for (int i = 0; i < cantidad; i++) {
            if (ordenes[i].getVehiculo().getPlaca().equalsIgnoreCase(v.getPlaca()) && ordenes[i].estaActiva()) {
                return true;
            }
        }
        return false;
    }
}
