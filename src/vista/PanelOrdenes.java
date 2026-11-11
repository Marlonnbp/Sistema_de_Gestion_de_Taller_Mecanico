package vista;

import gestores.Catalogo;
import gestores.GestorClientes;
import gestores.GestorOrdenes;
import gestores.GestorVehiculos;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class PanelOrdenes extends JPanel {

    private final GestorOrdenes gestorOrdenes;
    private final GestorClientes gestorClientes;
    private final GestorVehiculos gestorVehiculos;
    private final Catalogo catalogo;

    private JTextField txtDniCliente;
    private JTextField txtPlacaVehiculo;
    private JTextField txtMotivo;
    private JTextField txtBuscar;

    private JTable tablaOrdenes;
    private DefaultTableModel modeloTablaOrdenes;

    private JTable tablaItems;
    private DefaultTableModel modeloTablaItems;

    private JLabel lblTotalOrden;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public PanelOrdenes(GestorOrdenes gestorOrdenes, GestorClientes gestorClientes, GestorVehiculos gestorVehiculos, Catalogo catalogo) {
        this.gestorOrdenes = gestorOrdenes;
        this.gestorClientes = gestorClientes;
        this.gestorVehiculos = gestorVehiculos;
        this.catalogo = catalogo;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inicializarComponentes();
        cargarTablaOrdenes();
    }

    private void inicializarComponentes() {
        // --- CABECERA ---
        JPanel panelCabecera = new JPanel(new BorderLayout(10, 10));
        JLabel lblTitulo = new JLabel("Órdenes de Trabajo del Taller");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelCabecera.add(lblTitulo, BorderLayout.WEST);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        txtBuscar = new JTextField(12);
        JButton btnBuscar = new JButton("Buscar Orden");
        JButton btnTodos = new JButton("Ver Todas");

        panelBusqueda.add(new JLabel("Código / DNI / Placa:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnTodos);
        panelCabecera.add(panelBusqueda, BorderLayout.EAST);

        add(panelCabecera, BorderLayout.NORTH);

        // --- PANEL SUPERIOR: FORMULARIO CREACIÓN ---
        JPanel panelNuevaOrden = new JPanel(new GridBagLayout());
        panelNuevaOrden.setBorder(BorderFactory.createTitledBorder("Apertura de Nueva Orden de Trabajo"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDniCliente = new JTextField(10);
        txtPlacaVehiculo = new JTextField(10);
        txtMotivo = new JTextField(25);

        gbc.gridx = 0; gbc.gridy = 0;
        panelNuevaOrden.add(new JLabel("DNI Cliente:"), gbc);
        gbc.gridx = 1;
        panelNuevaOrden.add(txtDniCliente, gbc);

        gbc.gridx = 2;
        panelNuevaOrden.add(new JLabel("Placa Vehículo:"), gbc);
        gbc.gridx = 3;
        panelNuevaOrden.add(txtPlacaVehiculo, gbc);

        gbc.gridx = 4;
        panelNuevaOrden.add(new JLabel("Motivo / Falla:"), gbc);
        gbc.gridx = 5;
        panelNuevaOrden.add(txtMotivo, gbc);

        JButton btnCrearOrden = new JButton("Abrir Orden");
        btnCrearOrden.setBackground(new Color(40, 167, 69));
        btnCrearOrden.setForeground(Color.WHITE);
        gbc.gridx = 6;
        panelNuevaOrden.add(btnCrearOrden, gbc);

        // --- PANEL CENTRAL: TABLA DE ÓRDENES Y DETALLES ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.55);

        // Tabla Órdenes
        String[] colOrdenes = {"Código", "Fecha Ingreso", "Cliente", "Vehículo (Placa)", "Motivo", "Estado", "Total (S/.)"};
        modeloTablaOrdenes = new DefaultTableModel(colOrdenes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaOrdenes = new JTable(modeloTablaOrdenes);
        tablaOrdenes.setRowHeight(24);
        tablaOrdenes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollOrdenes = new JScrollPane(tablaOrdenes);
        scrollOrdenes.setBorder(BorderFactory.createTitledBorder("Listado de Órdenes"));

        // Panel Inferior: Items / Cotización y Acciones de la Orden
        JPanel panelInferior = new JPanel(new BorderLayout(8, 8));
        String[] colItems = {"Código", "Descripción", "Tipo", "Cantidad", "P. Unitario", "Subtotal"};
        modeloTablaItems = new DefaultTableModel(colItems, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaItems = new JTable(modeloTablaItems);
        tablaItems.setRowHeight(22);
        JScrollPane scrollItems = new JScrollPane(tablaItems);
        scrollItems.setBorder(BorderFactory.createTitledBorder("Servicios y Repuestos de la Orden Seleccionada"));
        panelInferior.add(scrollItems, BorderLayout.CENTER);

        // Barra de acciones para la orden seleccionada
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnAgregarItem = new JButton("➕ Agregar Servicio/Repuesto");
        JButton btnCambiarEstado = new JButton("⚙️ Cambiar Estado");
        JButton btnAnular = new JButton("❌ Anular Orden");
        lblTotalOrden = new JLabel("Total Orden: S/. 0.00   ");
        lblTotalOrden.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnAgregarItem.setBackground(new Color(23, 162, 184));
        btnAgregarItem.setForeground(Color.WHITE);
        btnCambiarEstado.setBackground(new Color(255, 193, 7));
        btnCambiarEstado.setForeground(Color.BLACK);
        btnAnular.setBackground(new Color(220, 53, 69));
        btnAnular.setForeground(Color.WHITE);

        panelAcciones.add(lblTotalOrden);
        panelAcciones.add(btnAgregarItem);
        panelAcciones.add(btnCambiarEstado);
        panelAcciones.add(btnAnular);
        panelInferior.add(panelAcciones, BorderLayout.SOUTH);

        splitPane.setTopComponent(scrollOrdenes);
        splitPane.setBottomComponent(panelInferior);

        JPanel panelCuerpo = new JPanel(new BorderLayout(8, 8));
        panelCuerpo.add(panelNuevaOrden, BorderLayout.NORTH);
        panelCuerpo.add(splitPane, BorderLayout.CENTER);

        add(panelCuerpo, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnCrearOrden.addActionListener(e -> crearOrden());
        btnBuscar.addActionListener(e -> buscarOrdenes());
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            cargarTablaOrdenes();
        });
        btnAgregarItem.addActionListener(e -> agregarItemAOrden());
        btnCambiarEstado.addActionListener(e -> cambiarEstadoOrden());
        btnAnular.addActionListener(e -> anularOrden());

        tablaOrdenes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarItemsDeOrdenSeleccionada();
            }
        });
    }

    private void crearOrden() {
        String dni = txtDniCliente.getText().trim();
        String placa = txtPlacaVehiculo.getText().trim();
        String motivo = txtMotivo.getText().trim();

        if (dni.isEmpty() || placa.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar DNI, Placa y Motivo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente c = gestorClientes.leer(dni);
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Cliente con DNI " + dni + " no existe o está inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehiculo v = gestorVehiculos.leer(placa);
        if (v == null) {
            JOptionPane.showMessageDialog(this, "Vehículo con placa " + placa + " no existe o está inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (gestorOrdenes.tieneOrdenActiva(v)) {
            JOptionPane.showMessageDialog(this, "El vehículo " + placa + " ya tiene una orden activa pendiente en el taller.", "Vehículo en Taller", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OrdenTrabajo nueva = gestorOrdenes.crear(c, v, motivo);
        if (nueva != null) {
            JOptionPane.showMessageDialog(this, "Orden creada con éxito: " + nueva.getCodigo(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            txtDniCliente.setText("");
            txtPlacaVehiculo.setText("");
            txtMotivo.setText("");
            cargarTablaOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo crear la orden: límite de capacidad alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarOrdenes() {
        String criterio = txtBuscar.getText().trim();
        if (criterio.isEmpty()) {
            cargarTablaOrdenes();
            return;
        }

        OrdenTrabajo[] resultado = gestorOrdenes.leer(criterio);
        mostrarEnTabla(resultado);
    }

    public void cargarTablaOrdenes() {
        OrdenTrabajo[] lista = gestorOrdenes.listar();
        mostrarEnTabla(lista);
    }

    private boolean esEstadoActivo(EstadoOrden estado) {
        return estado == EstadoOrden.EN_ESPERA ||
               estado == EstadoOrden.EN_DIAGNOSTICO ||
               estado == EstadoOrden.COTIZADA ||
               estado == EstadoOrden.APROBADA ||
               estado == EstadoOrden.LISTA_PARA_ENTREGA;
    }

    private void mostrarEnTabla(OrdenTrabajo[] lista) {
        modeloTablaOrdenes.setRowCount(0);
        for (OrdenTrabajo o : lista) {
            // Solo se muestran órdenes activas en taller; las FACTURADAS o ANULADAS van al Historial
            if (esEstadoActivo(o.getEstado())) {
                modeloTablaOrdenes.addRow(new Object[]{
                        o.getCodigo(),
                        sdf.format(o.getFechaIngreso()),
                        o.getCliente().getNombres(),
                        o.getVehiculo().getPlaca(),
                        o.getMotivoIngreso(),
                        o.getEstado(),
                        String.format("%.2f", o.calcularTotal())
                });
            }
        }
        modeloTablaItems.setRowCount(0);
        lblTotalOrden.setText("Total Orden: S/. 0.00   ");
    }

    private OrdenTrabajo obtenerOrdenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila < 0) return null;
        String codigo = modeloTablaOrdenes.getValueAt(fila, 0).toString();
        OrdenTrabajo[] busqueda = gestorOrdenes.leer(codigo);
        if (busqueda.length > 0) return busqueda[0];
        return null;
    }

    private void cargarItemsDeOrdenSeleccionada() {
        OrdenTrabajo orden = obtenerOrdenSeleccionada();
        modeloTablaItems.setRowCount(0);
        if (orden == null) {
            lblTotalOrden.setText("Total Orden: S/. 0.00   ");
            return;
        }

        ItemCotizacion[] items = orden.getItems();
        for (int i = 0; i < orden.getCantidadItems(); i++) {
            ItemCotizacion ic = items[i];
            String tipo = (ic.getReferencia() instanceof Servicio) ? "Servicio" : "Repuesto";
            modeloTablaItems.addRow(new Object[]{
                    ic.getReferencia().getCodigo(),
                    ic.getReferencia().getNombre(),
                    tipo,
                    ic.getCantidad(),
                    String.format("%.2f", ic.getPrecioUnitario()),
                    String.format("%.2f", ic.calcularSubtotal())
            });
        }

        lblTotalOrden.setText(String.format("Subtotal: S/. %.2f | IGV (18%%): S/. %.2f | Total: S/. %.2f   ",
                orden.calcularSubtotal(), orden.calcularIGV(), orden.calcularTotal()));
    }

    private void agregarItemAOrden() {
        OrdenTrabajo orden = obtenerOrdenSeleccionada();
        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione primero una orden de la lista.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!orden.estaActiva()) {
            JOptionPane.showMessageDialog(this, "No se pueden agregar ítems a una orden finalizada o anulada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // RF-17 Precondición: La orden debe estar en estado EN_DIAGNOSTICO (o COTIZADA para seguir añadiendo ítems)
        if (orden.getEstado() != EstadoOrden.EN_DIAGNOSTICO && orden.getEstado() != EstadoOrden.COTIZADA) {
            JOptionPane.showMessageDialog(this,
                    "Precondición no cumplida (RF-17 - Elaborar Cotización):\n" +
                    "Para cotizar servicios o repuestos, la orden debe estar en estado 'EN_DIAGNOSTICO'.\n" +
                    "Estado actual de la orden " + orden.getCodigo() + ": " + orden.getEstado() + ".\n" +
                    "Cambie primero el estado a 'EN_DIAGNOSTICO' con el botón '⚙️ Cambiar Estado'.",
                    "Precondición RF-17 no cumplida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemCatalogo[] disponibles = catalogo.listar();
        if (disponibles.length == 0) {
            JOptionPane.showMessageDialog(this, "El catálogo está vacío. Agregue servicios o repuestos en la pestaña Catálogo.", "Catálogo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Crear diálogo para elegir ítem y cantidad
        JComboBox<String> comboItems = new JComboBox<>();
        for (ItemCatalogo item : disponibles) {
            comboItems.addItem(item.getCodigo() + " - " + item.getNombre() + " (S/. " + item.getPrecio() + ")");
        }

        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        JPanel panelDialogo = new JPanel(new GridLayout(2, 2, 5, 5));
        panelDialogo.add(new JLabel("Seleccionar Ítem:"));
        panelDialogo.add(comboItems);
        panelDialogo.add(new JLabel("Cantidad:"));
        panelDialogo.add(spinnerCantidad);

        int opcion = JOptionPane.showConfirmDialog(this, panelDialogo, "Elaborar Cotización - Orden " + orden.getCodigo(), JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            int indice = comboItems.getSelectedIndex();
            ItemCatalogo seleccionado = disponibles[indice];
            int cant = (Integer) spinnerCantidad.getValue();

            // Validar existencia de stock al cotizar si es un repuesto
            if (seleccionado instanceof Repuesto) {
                Repuesto rep = (Repuesto) seleccionado;
                if (!rep.hayStock(cant)) {
                    JOptionPane.showMessageDialog(this,
                            "Stock insuficiente en almacén para cotizar este repuesto.\n" +
                            "Repuesto: " + rep.getNombre() + "\n" +
                            "Stock disponible: " + rep.getStock() + " unidades\n" +
                            "Cantidad requerida: " + cant + " unidades",
                            "Stock Insuficiente",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            orden.agregarItem(seleccionado, cant);

            // RF-17 Postcondición: La orden queda cotizada y pendiente de aprobación
            orden.cambiarEstado(EstadoOrden.COTIZADA);

            JOptionPane.showMessageDialog(this, 
                    "Ítem agregado con éxito a la cotización.\n" +
                    "La orden " + orden.getCodigo() + " ahora se encuentra en estado 'COTIZADA' (pendiente de aprobación).", 
                    "Cotización Elaborada (RF-17)", 
                    JOptionPane.INFORMATION_MESSAGE);
            cargarItemsDeOrdenSeleccionada();
            cargarTablaOrdenes();
        }
    }

    private void cambiarEstadoOrden() {
        OrdenTrabajo orden = obtenerOrdenSeleccionada();
        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la lista.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<EstadoOrden> comboEstados = new JComboBox<>(EstadoOrden.values());
        comboEstados.setSelectedItem(orden.getEstado());

        int res = JOptionPane.showConfirmDialog(this, comboEstados, "Cambiar estado de orden " + orden.getCodigo(), JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            EstadoOrden nuevoEstado = (EstadoOrden) comboEstados.getSelectedItem();

            // Momento A: Al APROBAR la orden se descuenta el stock de repuestos
            if (nuevoEstado == EstadoOrden.APROBADA && orden.getEstado() != EstadoOrden.APROBADA) {
                if (!orden.tieneStockDisponibleParaAprobar()) {
                    JOptionPane.showMessageDialog(this,
                            "No se puede aprobar la orden porque uno o más repuestos cotizados ya no cuentan con stock suficiente en el almacén.",
                            "Stock Insuficiente al Aprobar",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                orden.aprobar();
                JOptionPane.showMessageDialog(this,
                        "Orden " + orden.getCodigo() + " APROBADA exitosamente.\n" +
                        "Se han retirado y descontado del stock del almacén los repuestos cotizados.",
                        "Orden Aprobada - Stock Actualizado",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                orden.cambiarEstado(nuevoEstado);
            }

            cargarTablaOrdenes();
            cargarItemsDeOrdenSeleccionada();
        }
    }

    private void anularOrden() {
        OrdenTrabajo orden = obtenerOrdenSeleccionada();
        if (orden == null) {
            JOptionPane.showMessageDialog(this, "Seleccione la orden a anular.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int resp = JOptionPane.showConfirmDialog(this, "¿Está seguro de anular la orden " + orden.getCodigo() + "?", "Confirmar Anulación", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            boolean estabaAprobada = (orden.getEstado() == EstadoOrden.APROBADA || orden.getEstado() == EstadoOrden.LISTA_PARA_ENTREGA);
            boolean anulada = gestorOrdenes.anular(orden.getCodigo());
            if (anulada) {
                String msj = "Orden " + orden.getCodigo() + " anulada.";
                if (estabaAprobada) {
                    msj += "\nLos repuestos asignados fueron devueltos automáticamente al stock del almacén.";
                }
                JOptionPane.showMessageDialog(this, msj, "Aviso de Anulación", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaOrdenes();
                cargarItemsDeOrdenSeleccionada();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo anular la orden (quizás ya está cerrada o facturada).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
