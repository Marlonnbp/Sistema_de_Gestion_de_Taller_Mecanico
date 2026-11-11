package gestores;

import modelo.Cliente;

public class GestorClientes {
    private Cliente[] clientes;
    private int cantidad;

    public GestorClientes(int capacidadMaxima) {
        this.clientes = new Cliente[capacidadMaxima];
        this.cantidad = 0;
    }

    public boolean crear(Cliente c) {
        if (cantidad >= clientes.length) return false;
        
        // Evitar duplicados por DNI
        for (int i = 0; i < cantidad; i++) {
            if (clientes[i].getDni().equals(c.getDni())) {
                // Reactivación si estaba dado de baja
                if (!clientes[i].isActivo()) {
                    clientes[i] = c; 
                    return true;
                }
                return false; // Ya existe y está activo
            }
        }
        
        clientes[cantidad] = c;
        cantidad++;
        return true;
    }

    public Cliente leer(String dniONombre) {
        for (int i = 0; i < cantidad; i++) {
            if (clientes[i].isActivo()) {
                if (clientes[i].getDni().equals(dniONombre) || 
                    clientes[i].getNombres().equalsIgnoreCase(dniONombre)) {
                    return clientes[i];
                }
            }
        }
        return null;
    }

    public boolean actualizar(Cliente c) {
        for (int i = 0; i < cantidad; i++) {
            if (clientes[i].getDni().equals(c.getDni()) && clientes[i].isActivo()) {
                clientes[i] = c;
                return true;
            }
        }
        return false;
    }

    public boolean darDeBaja(String dni) {
        for (int i = 0; i < cantidad; i++) {
            if (clientes[i].getDni().equals(dni) && clientes[i].isActivo()) {
                clientes[i].setActivo(false);
                return true;
            }
        }
        return false;
    }
    
    public Cliente[] listarTodos() {
        Cliente[] copia = new Cliente[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = clientes[i];
        }
        return copia;
    }

    public Cliente[] listar() {
        return listarTodos();
    }

    public Cliente leerCualquiera(String dni) {
        if (dni == null) return null;
        for (int i = 0; i < cantidad; i++) {
            if (clientes[i].getDni().equals(dni)) {
                return clientes[i];
            }
        }
        return null;
    }
}