package principal;

import gestores.Catalogo;
import gestores.GestorClientes;
import gestores.GestorOrdenes;
import gestores.GestorVehiculos;
import gestores.HistorialFacturas;
import persistencia.GestorArchivos;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Gestion de Taller Mecanico...");

        // 1. Instanciar los gestores (definiendo la capacidad máxima de los arreglos)
        GestorClientes gestorClientes = new GestorClientes(100);
        Catalogo catalogo = new Catalogo(); // Su capacidad ya está definida internamente
        GestorVehiculos gestorVehiculos = new GestorVehiculos(100);
        GestorOrdenes gestorOrdenes = new GestorOrdenes(100);
        HistorialFacturas historialFacturas = new HistorialFacturas(100);

        // 2. Cargar los datos desde los archivos .txt (El orden de carga es CRÍTICO)
        System.out.println("Cargando base de datos...");
        GestorArchivos.cargarClientes(gestorClientes);
        GestorArchivos.cargarCatalogo(catalogo);
        GestorArchivos.cargarVehiculos(gestorVehiculos, gestorClientes);
        GestorArchivos.cargarOrdenes(gestorOrdenes, gestorClientes, gestorVehiculos, catalogo);
        GestorArchivos.cargarFacturas(historialFacturas, gestorOrdenes);
        System.out.println("Carga completada exitosamente.");

        // 3. Configurar el autoguardado al cerrar la aplicación (Shutdown Hook)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Guardando datos en los archivos .txt antes de salir...");
            GestorArchivos.guardarClientes(gestorClientes);
            GestorArchivos.guardarCatalogo(catalogo);
            GestorArchivos.guardarVehiculos(gestorVehiculos);
            GestorArchivos.guardarOrdenes(gestorOrdenes);
            GestorArchivos.guardarFacturas(historialFacturas);
            System.out.println("Datos persistidos correctamente.");
        }));

        // 4. Lanzar la Interfaz Gráfica
        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            
            // Inyectar las instancias únicas de los gestores a la ventana
            ventana.setGestores(gestorClientes, gestorVehiculos, catalogo, gestorOrdenes, historialFacturas);
            
            ventana.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            ventana.setVisible(true);
        });
    }
}
