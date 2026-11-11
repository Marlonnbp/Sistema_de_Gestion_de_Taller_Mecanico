/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import gestores.*;
import modelo.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Marlon
 */
public class VentanaPrincipal extends javax.swing.JFrame {
    
    // --- ATRIBUTOS PARA LOS GESTORES ---
    private gestores.GestorClientes gestorClientes;
    private gestores.GestorVehiculos gestorVehiculos;
    private gestores.Catalogo catalogo;
    private gestores.GestorOrdenes gestorOrdenes;
    private gestores.HistorialFacturas historialFacturas;

    // --- MÉTODO PARA INYECTAR LOS GESTORES ---
    public void setGestores(
            gestores.GestorClientes gClientes, 
            gestores.GestorVehiculos gVehiculos, 
            gestores.Catalogo gCatalogo, 
            gestores.GestorOrdenes gOrdenes, 
            gestores.HistorialFacturas hFacturas) {
        
        this.gestorClientes = gClientes;
        this.gestorVehiculos = gVehiculos;
        this.catalogo = gCatalogo;
        this.gestorOrdenes = gOrdenes;
        this.historialFacturas = hFacturas;
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    // Gestores centralizados del sistema
    private GestorClientes gestorClientes;
    private GestorVehiculos gestorVehiculos;
    private GestorOrdenes gestorOrdenes;
    private Catalogo catalogo;
    private HistorialFacturas historialFacturas;

    // Paneles de la interfaz
    private PanelClientes panelClientes;
    private PanelVehiculos panelVehiculos;
    private PanelOrdenes panelOrdenes;
    private PanelCatalogo panelCatalogo;

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        inicializarSistema();
    }

    private void inicializarSistema() {
        // 1. Instanciación de gestores con capacidad en arreglos (Algorítmica II)
        gestorClientes = new GestorClientes(100);
        gestorVehiculos = new GestorVehiculos(100);
        gestorOrdenes = new GestorOrdenes(100);
        catalogo = new Catalogo();
        historialFacturas = new HistorialFacturas(100);

        // 2. Datos de prueba iniciales en el catálogo para facilitar pruebas
        catalogo.crear(new Servicio("SRV-001", "Cambio de Aceite y Filtro", 85.0));
        catalogo.crear(new Servicio("SRV-002", "Alineación y Balanceo", 60.0));
        catalogo.crear(new Servicio("SRV-003", "Diagnóstico Computarizado", 75.0));
        catalogo.crear(new Servicio("SRV-004", "Mantenimiento del Sistema de Frenos", 120.0));
        catalogo.crear(new Repuesto("REP-001", "Filtro de Aceite Sintético", 35.0, 30));
        catalogo.crear(new Repuesto("REP-002", "Pastillas de Freno Delanteras", 140.0, 15));
        catalogo.crear(new Repuesto("REP-003", "Bujía de Iridio", 28.0, 40));
        catalogo.crear(new Repuesto("REP-004", "Líquido de Frenos DOT 4", 25.0, 20));

        // 3. Crear paneles de interfaz
        panelClientes = new PanelClientes(gestorClientes);
        panelVehiculos = new PanelVehiculos(gestorVehiculos, gestorClientes);
        panelOrdenes = new PanelOrdenes(gestorOrdenes, gestorClientes, gestorVehiculos, catalogo);
        panelCatalogo = new PanelCatalogo(catalogo, gestorOrdenes, historialFacturas);

        // 4. Configurar pestañas principales
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));

        pestañas.addTab("  👤 Clientes  ", panelClientes);
        pestañas.addTab("  🚗 Vehículos  ", panelVehiculos);
        pestañas.addTab("  📋 Órdenes de Trabajo  ", panelOrdenes);
        pestañas.addTab("  📦 Catálogo y Facturación  ", panelCatalogo);

        // Actualizar datos de las tablas automáticamente al cambiar de pestaña
        pestañas.addChangeListener(e -> {
            int idx = pestañas.getSelectedIndex();
            if (idx == 0) panelClientes.cargarTabla();
            else if (idx == 1) panelVehiculos.cargarTabla();
            else if (idx == 2) panelOrdenes.cargarTablaOrdenes();
            else if (idx == 3) {
                panelCatalogo.cargarTablaCatalogo();
                panelCatalogo.cargarTablaFacturas();
            }
        });

        // 5. Configurar contenedor y ventana
        setTitle("Sistema de Gestión de Taller Mecánico - Algorítmica II");
        setSize(1040, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pestañas, BorderLayout.CENTER);
        revalidate();
        repaint();
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
