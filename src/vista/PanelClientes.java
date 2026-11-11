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

public class PanelClientes extends JPanel {

    private final GestorClientes gestorClientes;
    private final GestorOrdenes gestorOrdenes;
    private final GestorVehiculos gestorVehiculos;

    private JTextField txtDni;
    private JTextField txtNombres;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtBuscar;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public PanelClientes(GestorClientes gestorClientes, GestorOrdenes gestorOrdenes, GestorVehiculos gestorVehiculos) {
        this.gestorClientes = gestorClientes;
        this.gestorOrdenes = gestorOrdenes;
        this.gestorVehiculos = gestorVehiculos;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        cargarTabla();
    }

    private void inicializarComponentes() {
        // --- PANEL SUPERIOR: TÍTULO Y BÚSQUEDA ---
        JPanel panelCabecera = new JPanel(new BorderLayout(10, 10));
        JLabel lblTitulo = new JLabel("Gestión de Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 37, 41));
        panelCabecera.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        txtBuscar = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnRestablecer = new JButton("Ver Todos");

        panelBusqueda.add(new JLabel("DNI o Nombre:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnRestablecer);
        panelCabecera.add(panelBusqueda, BorderLayout.EAST);

        add(panelCabecera, BorderLayout.NORTH);

        // --- PANEL CENTRAL: FORMULARIO Y TABLA ---
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));

        // Formulario a la izquierda
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        panelFormulario.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDni = new JTextField(15);
        txtNombres = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtCorreo = new JTextField(15);

        agregarCampo(panelFormulario, "DNI:", txtDni, gbc, 0);
        agregarCampo(panelFormulario, "Nombres:", txtNombres, gbc, 1);
        agregarCampo(panelFormulario, "Teléfono:", txtTelefono, gbc, 2);
        agregarCampo(panelFormulario, "Correo:", txtCorreo, gbc, 3);

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
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 6, 6, 6);
        panelFormulario.add(panelBotones, gbc);

        // Botón RF-06: Consultar vehículos del cliente
        JButton btnVerVehiculos = new JButton("🚗 Consultar Vehículos (RF-06)");
        btnVerVehiculos.setBackground(new Color(23, 162, 184));
        btnVerVehiculos.setForeground(Color.WHITE);
        btnVerVehiculos.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 5;
        gbc.insets = new Insets(6, 6, 6, 6);
        panelFormulario.add(btnVerVehiculos, gbc);

        panelCentral.add(panelFormulario, BorderLayout.WEST);

        // Tabla a la derecha
        String[] columnas = {"DNI", "Nombres y Apellidos", "Teléfono", "Correo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura en la tabla
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(24);
        tablaClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Clientes"));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        add(panelCentral, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnRegistrar.addActionListener(e -> registrarCliente());
        btnActualizar.addActionListener(e -> actualizarCliente());
        btnDarBaja.addActionListener(e -> darDeBajaCliente());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnVerVehiculos.addActionListener(e -> consultarVehiculosCliente());
        btnRestablecer.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTabla();
        });

        tablaClientes.addMouseListener(new MouseAdapter() {
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
        gbc.weightx = 0.3;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(campo, gbc);
    }

    private void registrarCliente() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (dni.isEmpty() || nombres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "DNI y Nombres son campos obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente nuevo = new Cliente(dni, nombres, telefono, correo);
        boolean registrado = gestorClientes.crear(nuevo);

        if (registrado) {
            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar: El DNI ya existe o se alcanzó la capacidad máxima.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente() {
        String dni = txtDni.getText().trim();
        String nombres = txtNombres.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (dni.isEmpty() || nombres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para actualizar y complete DNI y Nombres.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente clienteActualizado = new Cliente(dni, nombres, telefono, correo);
        boolean actualizado = gestorClientes.actualizar(clienteActualizado);

        if (actualizado) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente activo con el DNI indicado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBajaCliente() {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese o seleccione el DNI del cliente a dar de baja.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = gestorClientes.leer(dni);
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente activo con el DNI indicado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // RF-04: El sistema verifica que el cliente no tenga órdenes activas
        if (gestorOrdenes != null && gestorOrdenes.tieneOrdenesActivas(cliente)) {
            JOptionPane.showMessageDialog(this,
                    "Rechazo de baja (RF-04):\nNo se puede dar de baja al cliente '" + cliente.getNombres() + 
                    "' porque tiene órdenes de trabajo activas en el taller.",
                    "Baja Rechazada",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de dar de baja al cliente con DNI " + dni + " (" + cliente.getNombres() + ")?",
                "Confirmar Baja Lógica",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean dadoDeBaja = gestorClientes.darDeBaja(dni);
            if (dadoDeBaja) {
                JOptionPane.showMessageDialog(this, "Cliente dado de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo procesar la baja del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarCliente() {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            cargarTabla();
            return;
        }

        Cliente encontrado = gestorClientes.leer(criterio);
        modeloTabla.setRowCount(0); // Limpiar tabla
        if (encontrado != null) {
            modeloTabla.addRow(new Object[]{
                    encontrado.getDni(),
                    encontrado.getNombres(),
                    encontrado.getTelefono(),
                    encontrado.getCorreo(),
                    encontrado.isActivo() ? "Activo" : "Inactivo"
            });
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente activo con: " + criterio, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);
        Cliente[] lista = gestorClientes.listar();
        for (Cliente c : lista) {
            modeloTabla.addRow(new Object[]{
                    c.getDni(),
                    c.getNombres(),
                    c.getTelefono(),
                    c.getCorreo(),
                    c.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tablaClientes.getSelectedRow();
        if (fila >= 0) {
            txtDni.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombres.setText(modeloTabla.getValueAt(fila, 1).toString());
            Object tel = modeloTabla.getValueAt(fila, 2);
            txtTelefono.setText(tel != null ? tel.toString() : "");
            Object mail = modeloTabla.getValueAt(fila, 3);
            txtCorreo.setText(mail != null ? mail.toString() : "");
        }
    }

    private void limpiarFormulario() {
        txtDni.setText("");
        txtNombres.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tablaClientes.clearSelection();
    }

    // RF-06: Consultar vehículos del cliente
    private void consultarVehiculosCliente() {
        String dni = txtDni.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Seleccione un cliente de la tabla o ingrese su DNI para consultar sus vehículos.", 
                    "Validación (RF-06)", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Precondición: El cliente existe
        Cliente c = gestorClientes.leer(dni);
        if (c == null) {
            JOptionPane.showMessageDialog(this, 
                    "Precondición no cumplida (RF-06): El cliente con DNI " + dni + " no existe o está inactivo.", 
                    "Cliente no encontrado", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (gestorVehiculos == null) {
            JOptionPane.showMessageDialog(this, "Gestor de vehículos no disponible.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proceso: El sistema recupera los vehículos asociados
        Vehiculo[] vehiculos = gestorVehiculos.listarPorCliente(c);

        if (vehiculos.length == 0) {
            JOptionPane.showMessageDialog(this, 
                    "El cliente " + c.getNombres() + " (DNI: " + c.getDni() + ") no tiene vehículos asociados registrados actualmente.", 
                    "Vehículos del Cliente (RF-06)", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Salidas / Postcondición: La lista se muestra al usuario
        String[] columnas = {"Placa", "Año", "Color", "Kilometraje", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        for (Vehiculo v : vehiculos) {
            model.addRow(new Object[]{
                    v.getPlaca(),
                    v.getAnio(),
                    v.getColor(),
                    v.getKilometraje() + " km",
                    v.isActivo() ? "Activo" : "Inactivo"
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(520, 180));

        JOptionPane.showMessageDialog(this, scroll, 
                "Vehículos de " + c.getNombres() + " (DNI: " + c.getDni() + ") - RF-06", 
                JOptionPane.PLAIN_MESSAGE);
    }
}

