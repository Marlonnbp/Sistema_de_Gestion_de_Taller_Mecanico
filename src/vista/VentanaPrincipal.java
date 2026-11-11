/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import gestores.*;
import modelo.*;
import persistencia.GestorArchivos;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Marlon
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    // --- ATRIBUTOS PARA LOS GESTORES ---
    private GestorClientes gestorClientes;
    private GestorVehiculos gestorVehiculos;
    private Catalogo catalogo;
    private GestorOrdenes gestorOrdenes;
    private HistorialFacturas historialFacturas;

    // --- PANELES DE LA INTERFAZ ---
    private PanelClientes panelClientes;
    private PanelVehiculos panelVehiculos;
    private PanelOrdenes panelOrdenes;
    private PanelCatalogo panelCatalogo;

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
    }

    // --- MÉTODO PARA INYECTAR LOS GESTORES (Llamado desde Main tras cargar .txt) ---
    public void setGestores(
            GestorClientes gClientes, 
            GestorVehiculos gVehiculos, 
            Catalogo gCatalogo, 
            GestorOrdenes gOrdenes, 
            HistorialFacturas hFacturas) {
        
        this.gestorClientes = gClientes;
        this.gestorVehiculos = gVehiculos;
        this.catalogo = gCatalogo;
        this.gestorOrdenes = gOrdenes;
        this.historialFacturas = hFacturas;

        construirInterfaz();
    }

    private void construirInterfaz() {
        // 1. Crear paneles de interfaz conectados a los gestores cargados
        panelClientes = new PanelClientes(gestorClientes);
        panelVehiculos = new PanelVehiculos(gestorVehiculos, gestorClientes);
        panelOrdenes = new PanelOrdenes(gestorOrdenes, gestorClientes, gestorVehiculos, catalogo);
        panelCatalogo = new PanelCatalogo(catalogo, gestorOrdenes, historialFacturas);

        // 2. Configurar pestañas principales
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));

        pestañas.addTab("  👤 Clientes  ", panelClientes);
        pestañas.addTab("  🚗 Vehículos  ", panelVehiculos);
        pestañas.addTab("  📋 Órdenes de Trabajo  ", panelOrdenes);
        pestañas.addTab("  📦 Catálogo e Historial  ", panelCatalogo);

        // Actualizar datos de las tablas automáticamente al cambiar de pestaña
        pestañas.addChangeListener(e -> {
            int idx = pestañas.getSelectedIndex();
            if (idx == 0) panelClientes.cargarTabla();
            else if (idx == 1) panelVehiculos.cargarTabla();
            else if (idx == 2) panelOrdenes.cargarTablaOrdenes();
            else if (idx == 3) {
                panelCatalogo.cargarTablaCatalogo();
                panelCatalogo.cargarTablaHistorial();
            }
        });

        // 3. Barra de Menú con opción para Guardar en Archivos .txt
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JMenuItem itemGuardar = new JMenuItem("💾 Guardar Todo en Archivos (.txt)");
        itemGuardar.addActionListener(e -> guardarDatosEnDisco());

        JMenuItem itemSalir = new JMenuItem("❌ Salir");
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemGuardar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);

        // 4. Configurar dimensiones y visualización de la ventana
        setTitle("Sistema de Gestión de Taller Mecánico - Algorítmica II");
        setSize(1040, 720);
        setMinimumSize(new Dimension(900, 600));

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pestañas, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void guardarDatosEnDisco() {
        try {
            GestorArchivos.guardarClientes(gestorClientes);
            GestorArchivos.guardarCatalogo(catalogo);
            GestorArchivos.guardarVehiculos(gestorVehiculos);
            GestorArchivos.guardarOrdenes(gestorOrdenes);
            GestorArchivos.guardarFacturas(historialFacturas);

            JOptionPane.showMessageDialog(this,
                    "¡Todos los datos han sido guardados exitosamente en la carpeta 'datos/'!",
                    "Guardado Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al guardar los archivos: " + ex.getMessage(),
                    "Error al Guardar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
