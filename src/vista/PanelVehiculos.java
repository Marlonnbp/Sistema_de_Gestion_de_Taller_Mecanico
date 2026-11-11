package vista;

import gestores.GestorClientes;
import gestores.GestorOrdenes;
import gestores.GestorVehiculos;
import modelo.Cliente;
import modelo.Vehiculo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelVehiculos extends JPanel {

    private final GestorVehiculos gestorVehiculos;
    private final GestorClientes gestorClientes;
    private final GestorOrdenes gestorOrdenes;

    private JTextField txtPlaca;
    private JTextField txtAnio;
    private JTextField txtColor;
    private JTextField txtKilometraje;
    private JTextField txtDniPropietario;
    private JTextField txtBuscarPlaca;

    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;

    public PanelVehiculos(GestorVehiculos gestorVehiculos, GestorClientes gestorClientes, GestorOrdenes gestorOrdenes) {
        this.gestorVehiculos = gestorVehiculos;
        this.gestorClientes = gestorClientes;
        this.gestorOrdenes = gestorOrdenes;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        cargarTabla();
    }

    private void inicializarComponentes() {
        // --- PANEL SUPERIOR: TÍTULO Y BÚSQUEDA ---
        JPanel panelCabecera = new JPanel(new BorderLayout(10, 10));
        JLabel lblTitulo = new JLabel("Gestión de Vehículos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 37, 41));
        panelCabecera.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        txtBuscarPlaca = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRestablecer = new JButton("Ver Todos");

        panelBusqueda.add(new JLabel("Placa o DNI Dueño:"));
        panelBusqueda.add(txtBuscarPlaca);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnRestablecer);
        panelCabecera.add(panelBusqueda, BorderLayout.EAST);

        add(panelCabecera, BorderLayout.NORTH);

        // --- PANEL CENTRAL: FORMULARIO Y TABLA ---
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));

        // Formulario a la izquierda
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Vehículo"));
        panelFormulario.setPreferredSize(new Dimension(340, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtPlaca = new JTextField(15);
        txtAnio = new JTextField(15);
        txtColor = new JTextField(15);
        txtKilometraje = new JTextField(15);
        txtDniPropietario = new JTextField(15);

        agregarCampo(panelFormulario, "Placa:", txtPlaca, gbc, 0);
        agregarCampo(panelFormulario, "Año:", txtAnio, gbc, 1);
        agregarCampo(panelFormulario, "Color:", txtColor, gbc, 2);
        agregarCampo(panelFormulario, "Kilometraje:", txtKilometraje, gbc, 3);
        agregarCampo(panelFormulario, "DNI Dueño:", txtDniPropietario, gbc, 4);

        // Panel de botones del formulario
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 6, 6));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnDarBaja = new JButton("Dar de Baja");
        JButton btnLimpiar = new JButton("Limpiar");

        btnRegistrar.setBackground(new Color(40, 167, 69));
        btnRegistrar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(0, 123, 255));
        btnActualizar.setForeground(Color.WHITE);
        btnDarBaja.setBackground(new Color(220, 53, 69));
        btnDarBaja.setForeground(Color.WHITE);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnDarBaja);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 6, 6, 6);
        panelFormulario.add(panelBotones, gbc);

        panelCentral.add(panelFormulario, BorderLayout.WEST);

        // Tabla a la derecha
        String[] columnas = {"Placa", "Año", "Color", "Km", "Propietario", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaVehiculos = new JTable(modeloTabla);
        tablaVehiculos.setRowHeight(24);
        tablaVehiculos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaVehiculos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Vehículos en Taller"));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnRegistrar.addActionListener(e -> registrarVehiculo());
        btnActualizar.addActionListener(e -> actualizarVehiculo());
        btnDarBaja.addActionListener(e -> darDeBajaVehiculo());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarVehiculo());
        btnRestablecer.addActionListener(e -> {
            txtBuscarPlaca.setText("");
            cargarTabla();
        });

        tablaVehiculos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarFormularioDesdeTabla();
            }
        });
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo, GridBagConstraints gbc, int fila) {
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0.35;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(campo, gbc);
    }

    private void registrarVehiculo() {
        String placa = txtPlaca.getText().trim();
        String strAnio = txtAnio.getText().trim();
        String color = txtColor.getText().trim();
        String strKm = txtKilometraje.getText().trim();
        String dniDueño = txtDniPropietario.getText().trim();

        if (placa.isEmpty() || strAnio.isEmpty() || color.isEmpty() || strKm.isEmpty() || dniDueño.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int anio;
        int kilometraje;
        try {
            anio = Integer.parseInt(strAnio);
            kilometraje = Integer.parseInt(strKm);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y Kilometraje deben ser valores numéricos enteros.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente propietario = gestorClientes.leer(dniDueño);
        if (propietario == null) {
            JOptionPane.showMessageDialog(this, "No existe un cliente activo con el DNI: " + dniDueño + ". Regístrelo primero en la pestaña Clientes.", "Propietario no encontrado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehiculo nuevo = new Vehiculo(placa, anio, color, kilometraje, propietario);
        boolean registrado = gestorVehiculos.crear(nuevo);

        if (registrado) {
            JOptionPane.showMessageDialog(this, "Vehículo registrado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar: La placa ya existe o se alcanzó la capacidad máxima.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVehiculo() {
        String placa = txtPlaca.getText().trim();
        String strAnio = txtAnio.getText().trim();
        String color = txtColor.getText().trim();
        String strKm = txtKilometraje.getText().trim();
        String dniDueño = txtDniPropietario.getText().trim();

        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo e ingrese la placa a actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int anio;
        int kilometraje;
        try {
            anio = Integer.parseInt(strAnio);
            kilometraje = Integer.parseInt(strKm);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y Kilometraje deben ser numéricos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente propietario = gestorClientes.leer(dniDueño);
        if (propietario == null) {
            JOptionPane.showMessageDialog(this, "No existe cliente con DNI " + dniDueño, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehiculo actualizado = new Vehiculo(placa, anio, color, kilometraje, propietario);
        boolean ok = gestorVehiculos.actualizar(actualizado);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Vehículo actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el vehículo activo con esa placa.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBajaVehiculo() {
        String placa = txtPlaca.getText().trim();
        if (placa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione o ingrese la placa a dar de baja.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Vehiculo v = gestorVehiculos.leer(placa);
        if (v == null) {
            JOptionPane.showMessageDialog(this, "No se encontró un vehículo activo con la placa indicada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // El sistema verifica que no tenga órdenes activas
        if (gestorOrdenes != null && gestorOrdenes.tieneOrdenActiva(v)) {
            JOptionPane.showMessageDialog(this,
                    "No se puede dar de baja al vehículo con placa " + placa + 
                    " porque tiene una orden de trabajo activa en el taller.",
                    "Baja Rechazada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de dar de baja al vehículo con placa " + placa + "?",
                "Confirmar Baja Lógica",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean dadoDeBaja = gestorVehiculos.darDeBaja(placa);
            if (dadoDeBaja) {
                JOptionPane.showMessageDialog(this, "Vehículo dado de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo procesar la baja del vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarVehiculo() {
        String criterio = txtBuscarPlaca.getText().trim();
        if (criterio.isEmpty()) {
            cargarTabla();
            return;
        }

        // 1. Intentar buscar por placa exacta
        Vehiculo v = gestorVehiculos.leer(criterio);
        modeloTabla.setRowCount(0);
        if (v != null) {
            modeloTabla.addRow(new Object[]{
                    v.getPlaca(),
                    v.getAnio(),
                    v.getColor(),
                    v.getKilometraje(),
                    v.getPropietario() != null ? (v.getPropietario().getDni() + " - " + v.getPropietario().getNombres()) : "Sin dueño",
                    v.isActivo() ? "Activo" : "Inactivo"
            });
            return;
        }

        // 2. Intentar buscar si el criterio es el DNI de un cliente (RF-06)
        Cliente c = gestorClientes.leer(criterio);
        if (c != null) {
            Vehiculo[] listaCliente = gestorVehiculos.listarPorCliente(c);
            if (listaCliente.length > 0) {
                for (Vehiculo veh : listaCliente) {
                    modeloTabla.addRow(new Object[]{
                            veh.getPlaca(),
                            veh.getAnio(),
                            veh.getColor(),
                            veh.getKilometraje(),
                            veh.getPropietario() != null ? (veh.getPropietario().getDni() + " - " + veh.getPropietario().getNombres()) : "Sin dueño",
                            veh.isActivo() ? "Activo" : "Inactivo"
                    });
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "No se encontró ningún vehículo activo por placa o DNI de cliente: " + criterio, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        Vehiculo[] lista = gestorVehiculos.listar();
        for (Vehiculo v : lista) {
            modeloTabla.addRow(new Object[]{
                    v.getPlaca(),
                    v.getAnio(),
                    v.getColor(),
                    v.getKilometraje(),
                    v.getPropietario() != null ? (v.getPropietario().getDni() + " - " + v.getPropietario().getNombres()) : "Sin dueño",
                    v.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila >= 0) {
            txtPlaca.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtAnio.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtColor.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtKilometraje.setText(modeloTabla.getValueAt(fila, 3).toString());

            String propInfo = modeloTabla.getValueAt(fila, 4).toString();
            if (propInfo.contains(" - ")) {
                txtDniPropietario.setText(propInfo.split(" - ")[0]);
            }
        }
    }

    private void limpiarFormulario() {
        txtPlaca.setText("");
        txtAnio.setText("");
        txtColor.setText("");
        txtKilometraje.setText("");
        txtDniPropietario.setText("");
        tablaVehiculos.clearSelection();
    }
}

