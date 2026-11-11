package gestores;

import modelo.Cliente;
import modelo.Vehiculo;

public class GestorVehiculos {
    private Vehiculo[] vehiculos;
    private int cantidad;

    public GestorVehiculos(int capacidadMaxima) {
        this.vehiculos = new Vehiculo[capacidadMaxima];
        this.cantidad = 0;
    }

    public boolean crear(Vehiculo v) {
        if (cantidad >= vehiculos.length) return false;
        
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].getPlaca().equalsIgnoreCase(v.getPlaca())) {
                // Reactivación si estaba dado de baja
                if (!vehiculos[i].isActivo()) {
                    vehiculos[i] = v; 
                    return true;
                }
                return false;
            }
        }
        
        vehiculos[cantidad] = v;
        cantidad++;
        return true;
    }

    public Vehiculo leer(String placa) {
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].isActivo() && vehiculos[i].getPlaca().equalsIgnoreCase(placa)) {
                return vehiculos[i];
            }
        }
        return null;
    }

    public Vehiculo[] listarPorCliente(Cliente c) {
        int coincidencias = 0;
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].isActivo() && vehiculos[i].getPropietario().getDni().equals(c.getDni())) {
                coincidencias++;
            }
        }

        Vehiculo[] resultado = new Vehiculo[coincidencias];
        int indice = 0;
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].isActivo() && vehiculos[i].getPropietario().getDni().equals(c.getDni())) {
                resultado[indice] = vehiculos[i];
                indice++;
            }
        }
        return resultado;
    }

    public boolean actualizar(Vehiculo v) {
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].getPlaca().equalsIgnoreCase(v.getPlaca()) && vehiculos[i].isActivo()) {
                vehiculos[i] = v;
                return true;
            }
        }
        return false;
    }

    public boolean darDeBaja(String placa) {
        for (int i = 0; i < cantidad; i++) {
            if (vehiculos[i].getPlaca().equalsIgnoreCase(placa) && vehiculos[i].isActivo()) {
                vehiculos[i].setActivo(false);
                return true;
            }
        }
        return false;
    }
}
