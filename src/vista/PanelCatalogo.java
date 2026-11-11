package vista;

import gestores.Catalogo;
import gestores.GestorOrdenes;
import gestores.HistorialFacturas;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;

public class PanelCatalogo extends JPanel {

    private final Catalogo catalogo;
    private final GestorOrdenes gestorOrdenes;
    private final HistorialFacturas historialFacturas;

    // Componentes de Catálogo
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JComboBox<String> cmbTipo;
    private JTextField txtBuscarNombre;
    private JTable tablaCatalogo;
    private DefaultTableModel modeloTablaCatalogo;

    // Componentes de Historial
    private JTable tablaHistorial;
    private DefaultTableModel modeloTablaHistorial;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public PanelCatalogo(Catalogo catalogo, GestorOrdenes gestorOrdenes, HistorialFacturas historialFacturas) {
        this.catalogo = catalogo;
        this.gestorOrdenes = gestorOrdenes;
        this.historialFacturas = historialFacturas;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.addTab("📦 Catálogo de Ítems (Servicios / Repuestos)", crearPanelCatalogo());
        subTabs.addTab("📜 Historial", crearPanelHistorial());

        add(subTabs, BorderLayout.CENTER);

        cargarTablaCatalogo();
        cargarTablaHistorial();
    }

    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Cabecera con búsqueda
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtBuscarNombre = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnTodos = new JButton("Ver Todos");

        panelCabecera.add(new JLabel("Buscar por nombre:"));
        panelCabecera.add(txtBuscarNombre);
        panelCabecera.add(btnBuscar);
        panelCabecera.add(btnTodos);
        panel.add(panelCabecera, BorderLayout.NORTH);

        // Centro: Formulario a la izquierda y Tabla al centro
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nuevo Ítem"));
        panelFormulario.setPreferredSize(new Dimension(320, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCodigo = new JTextField(12);
        txtNombre = new JTextField(12);
        txtPrecio = new JTextField(12);
        txtStock = new JTextField(12);
        cmbTipo = new JComboBox<>(new String[]{"SERVICIO", "REPUESTO"});
        txtStock.setEnabled(false); // Por defecto SERVICIO no tiene stock

        cmbTipo.addActionListener(e -> {
            boolean esRepuesto = "REPUESTO".equals(cmbTipo.getSelectedItem());
            txtStock.setEnabled(esRepuesto);
            if (!esRepuesto) txtStock.setText("");
        });

        agregarCampo(panelFormulario, "Tipo:", cmbTipo, gbc, 0);
        agregarCampo(panelFormulario, "Código:", txtCodigo, gbc, 1);
        agregarCampo(panelFormulario, "Nombre:", txtNombre, gbc, 2);
        agregarCampo(panelFormulario, "Precio (S/.):", txtPrecio, gbc, 3);
        agregarCampo(panelFormulario, "Stock inicial:", txtStock, gbc, 4);

        JButton btnGuardar = new JButton("Guardar Ítem");
        JButton btnActualizar = new JButton("Actualizar (RF-15)");
        JButton btnBaja = new JButton("Dar de Baja");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnActualizar.setBackground(new Color(0, 123, 255));
        btnActualizar.setForeground(Color.WHITE);
        btnBaja.setBackground(new Color(220, 53, 69));
        btnBaja.setForeground(Color.WHITE);

        JPanel panelBtns = new JPanel(new GridLayout(2, 2, 6, 6));
        panelBtns.add(btnGuardar);
        panelBtns.add(btnActualizar);
        panelBtns.add(btnBaja);
        panelBtns.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 6, 6, 6);
        panelFormulario.add(panelBtns, gbc);

        panel.add(panelFormulario, BorderLayout.WEST);

        // Tabla
        String[] columnas = {"Tipo", "Código", "Descripción", "Precio (S/.)", "Stock", "Estado"};
        modeloTablaCatalogo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCatalogo = new JTable(modeloTablaCatalogo);
        tablaCatalogo.setRowHeight(24);
        tablaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaCatalogo), BorderLayout.CENTER);

        // Eventos
        btnGuardar.addActionListener(e -> guardarItem());
        btnActualizar.addActionListener(e -> actualizarItem());
        btnBaja.addActionListener(e -> darBajaItem());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarItem());
        btnTodos.addActionListener(e -> {
            txtBuscarNombre.setText("");
            cargarTablaCatalogo();
        });

        tablaCatalogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarFormularioDesdeTabla();
            }
        });

        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnEmitirFactura = new JButton("🧾 Emitir Factura para Orden...");
        JButton btnVerTicket = new JButton("👁️ Ver Ticket de Factura");

        btnEmitirFactura.setBackground(new Color(0, 123, 255));
        btnEmitirFactura.setForeground(Color.WHITE);

        panelAcciones.add(btnEmitirFactura);
        panelAcciones.add(btnVerTicket);
        panel.add(panelAcciones, BorderLayout.NORTH);

        // Columnas actualizadas incluyendo el Estado (FACTURADA / ANULADA)
        String[] cols = {"N° Orden", "Estado", "N° Factura", "Fecha Cierre", "Cliente", "Vehículo (Placa)", "Total (S/.)"};
        modeloTablaHistorial = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaHistorial = new JTable(modeloTablaHistorial);
        tablaHistorial.setRowHeight(24);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        btnEmitirFactura.addActionListener(e -> emitirFacturaDialogo());
        btnVerTicket.addActionListener(e -> verTicketFactura());

        return panel;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JComponent campo, GridBagConstraints gbc, int fila) {
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.weightx = 0.35;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.65;
        panel.add(campo, gbc);
    }

    private void guardarItem() {
        String tipo = (String) cmbTipo.getSelectedItem();
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String strPrecio = txtPrecio.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || strPrecio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(strPrecio);
            if (precio <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número positivo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemCatalogo nuevo;
        if ("REPUESTO".equals(tipo)) {
            String strStock = txtStock.getText().trim();
            int stock;
            try {
                stock = Integer.parseInt(strStock);
                if (stock < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El stock debe ser un entero mayor o igual a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            nuevo = new Repuesto(codigo, nombre, precio, stock);
        } else {
            nuevo = new Servicio(codigo, nombre, precio);
        }

        boolean ok = catalogo.crear(nuevo);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Ítem agregado al catálogo con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarTablaCatalogo();
        } else {
            JOptionPane.showMessageDialog(this, "El código ya existe o el catálogo está lleno.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // RF-15: Modificar repuesto o servicio
    private void actualizarItem() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String strPrecio = txtPrecio.getText().trim();
        String tipo = (String) cmbTipo.getSelectedItem();

        if (codigo.isEmpty() || nombre.isEmpty() || strPrecio.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Seleccione un ítem del catálogo o ingrese sus datos para actualizar.", 
                    "Validación (RF-15)", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // RF-15 Precondición: El ítem existe
        ItemCatalogo existente = catalogo.leer(codigo);
        if (existente == null) {
            JOptionPane.showMessageDialog(this, 
                    "Precondición no cumplida (RF-15): El ítem con código '" + codigo + "' no existe en el catálogo activo.", 
                    "Ítem no encontrado", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double nuevoPrecio;
        try {
            nuevoPrecio = Double.parseDouble(strPrecio);
            if (nuevoPrecio < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número mayor o igual a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int nuevoStock = -1;
        if ("REPUESTO".equals(tipo)) {
            String strStock = txtStock.getText().trim();
            try {
                nuevoStock = Integer.parseInt(strStock);
                if (nuevoStock < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El stock de repuesto debe ser un número entero mayor o igual a 0.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // RF-15 Proceso: Actualizar valores
        boolean actualizado = catalogo.actualizar(codigo, nombre, nuevoPrecio, nuevoStock);
        if (actualizado) {
            // RF-15 Postcondición: Muestra valores actualizados (cotizaciones previas congelan sus precios)
            JOptionPane.showMessageDialog(this, 
                    "Ítem '" + codigo + "' actualizado correctamente (RF-15).\n" +
                    "(Las cotizaciones ya emitidas conservan sus precios congelados intactos).", 
                    "Actualización Exitosa", 
                    JOptionPane.INFORMATION_MESSAGE);
            cargarTablaCatalogo();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el ítem.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFormularioDesdeTabla() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila >= 0) {
            String tipo = modeloTablaCatalogo.getValueAt(fila, 0).toString();
            cmbTipo.setSelectedItem(tipo);
            txtCodigo.setText(modeloTablaCatalogo.getValueAt(fila, 1).toString());
            txtNombre.setText(modeloTablaCatalogo.getValueAt(fila, 2).toString());
            txtPrecio.setText(modeloTablaCatalogo.getValueAt(fila, 3).toString().replace(",", "."));

            Object stockVal = modeloTablaCatalogo.getValueAt(fila, 4);
            if ("REPUESTO".equals(tipo) && stockVal != null && !stockVal.equals("N/A")) {
                txtStock.setText(stockVal.toString());
                txtStock.setEnabled(true);
            } else {
                txtStock.setText("");
                txtStock.setEnabled(false);
            }
        }
    }

    private void limpiarFormulario() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        tablaCatalogo.clearSelection();
    }

    private void darBajaItem() {
        int fila = tablaCatalogo.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un ítem de la tabla para dar de baja.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = modeloTablaCatalogo.getValueAt(fila, 1).toString();
        int resp = JOptionPane.showConfirmDialog(this, "¿Dar de baja el ítem " + codigo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            boolean ok = catalogo.darDeBaja(codigo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Ítem dado de baja correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaCatalogo();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo dar de baja.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarItem() {
        String texto = txtBuscarNombre.getText().trim();
        if (texto.isEmpty()) {
            cargarTablaCatalogo();
            return;
        }
        ItemCatalogo[] lista = catalogo.buscarPorNombre(texto);
        mostrarEnTablaCatalogo(lista);
    }

    public void cargarTablaCatalogo() {
        mostrarEnTablaCatalogo(catalogo.listar());
    }

    private void mostrarEnTablaCatalogo(ItemCatalogo[] lista) {
        modeloTablaCatalogo.setRowCount(0);
        for (ItemCatalogo item : lista) {
            String stockStr = (item instanceof Repuesto) ? String.valueOf(((Repuesto) item).getStock()) : "N/A";
            modeloTablaCatalogo.addRow(new Object[]{
                    item.getTipo(),
                    item.getCodigo(),
                    item.getNombre(),
                    String.format("%.2f", item.getPrecio()),
                    stockStr,
                    item.isActivo() ? "Activo" : "Inactivo"
            });
        }
    }

    private boolean tieneFacturaEmitida(OrdenTrabajo orden) {
        for (Factura f : historialFacturas.listarTodos()) {
            if (f.getOrden() != null && f.getOrden().getCodigo().equalsIgnoreCase(orden.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    private void emitirFacturaDialogo() {
        OrdenTrabajo[] todasLasOrdenes = gestorOrdenes.listarTodos();
        
        // REGLA DE NEGOCIO: Solo se puede emitir factura si el estado de la orden es FACTURADA y no tiene factura previa
        int cantFacturables = 0;
        for (OrdenTrabajo o : todasLasOrdenes) {
            if (o.getEstado() == EstadoOrden.FACTURADA && !tieneFacturaEmitida(o)) {
                cantFacturables++;
            }
        }

        if (cantFacturables == 0) {
            JOptionPane.showMessageDialog(this, 
                    "Regla de Negocio:\n" +
                    "Solo se puede emitir una factura si el estado de la orden es FACTURADA y está pendiente de comprobante.\n" +
                    "Cambie el estado de la orden a FACTURADA en la pestaña 'Órdenes de Trabajo' para poder emitir la factura.", 
                    "Regla de Negocio - Emisión de Factura", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<String> comboOrdenes = new JComboBox<>();
        OrdenTrabajo[] facturables = new OrdenTrabajo[cantFacturables];
        int k = 0;
        for (OrdenTrabajo o : todasLasOrdenes) {
            if (o.getEstado() == EstadoOrden.FACTURADA && !tieneFacturaEmitida(o)) {
                facturables[k++] = o;
                comboOrdenes.addItem(o.getCodigo() + " - " + o.getCliente().getNombres() + " (Total: S/. " + String.format("%.2f", o.calcularTotal()) + ") [Estado: FACTURADA]");
            }
        }

        int opt = JOptionPane.showConfirmDialog(this, comboOrdenes, "Seleccione la orden FACTURADA a emitir comprobante:", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            int idx = comboOrdenes.getSelectedIndex();
            OrdenTrabajo seleccionada = facturables[idx];

            if (seleccionada.getCantidadItems() == 0) {
                JOptionPane.showMessageDialog(this, "La orden no tiene ningún servicio o repuesto cotizado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int numFacturas = historialFacturas.listarTodos().length + 1;
            String numeroFactura = "FAC-" + String.format("%04d", numFacturas);
            Factura f = new Factura(numeroFactura);
            f.emitir(seleccionada);

            boolean registrada = historialFacturas.crear(f);
            if (registrada) {
                JOptionPane.showMessageDialog(this, "Factura " + numeroFactura + " emitida con éxito.", "Facturación", JOptionPane.INFORMATION_MESSAGE);
                cargarTablaHistorial();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la factura en el historial.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void cargarTablaHistorial() {
        modeloTablaHistorial.setRowCount(0);
        OrdenTrabajo[] todasLasOrdenes = gestorOrdenes.listarTodos();
        Factura[] facturas = historialFacturas.listarTodos();

        for (OrdenTrabajo o : todasLasOrdenes) {
            // Solo se muestran las órdenes que ya concluyeron su ciclo: FACTURADAS o ANULADAS
            if (o.getEstado() == EstadoOrden.FACTURADA || o.getEstado() == EstadoOrden.ANULADA) {
                Factura fAsociada = null;
                for (Factura f : facturas) {
                    if (f.getOrden() != null && f.getOrden().getCodigo().equalsIgnoreCase(o.getCodigo())) {
                        fAsociada = f;
                        break;
                    }
                }

                String numFactura = (fAsociada != null) ? fAsociada.getNumero() : (o.getEstado() == EstadoOrden.FACTURADA ? "Pendiente Comprobante" : "N/A - Cancelada");
                String fecha = (fAsociada != null && fAsociada.getFechaEmision() != null)
                        ? sdf.format(fAsociada.getFechaEmision())
                        : sdf.format(o.getFechaIngreso());
                String nomCliente = (o.getCliente() != null) ? o.getCliente().getNombres() : "-";
                String placaVehiculo = (o.getVehiculo() != null) ? o.getVehiculo().getPlaca() : "-";

                modeloTablaHistorial.addRow(new Object[]{
                        o.getCodigo(),
                        o.getEstado().toString(),
                        numFactura,
                        fecha,
                        nomCliente,
                        placaVehiculo,
                        String.format("%.2f", o.calcularTotal())
                });
            }
        }
    }

    private void verTicketFactura() {
        int fila = tablaHistorial.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden del historial para ver su comprobante.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String estado = modeloTablaHistorial.getValueAt(fila, 1).toString();
        if ("ANULADA".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, 
                    "Esta orden fue anulada/cancelada, por lo que no cuenta con un comprobante de pago emitido.", 
                    "Sin Comprobante", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigoOrden = modeloTablaHistorial.getValueAt(fila, 0).toString();
        Factura[] lista = historialFacturas.listarTodos();
        Factura f = null;
        for (Factura fac : lista) {
            if (fac.getOrden() != null && fac.getOrden().getCodigo().equalsIgnoreCase(codigoOrden)) {
                f = fac;
                break;
            }
        }

        if (f == null) {
            JOptionPane.showMessageDialog(this, 
                    "Esta orden está en estado FACTURADA pero aún no se ha generado su comprobante formal.\n" +
                    "Use el botón 'Emitir Factura para Orden...' para generar el número de factura y su comprobante.", 
                    "Comprobante no generado", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        OrdenTrabajo o = f.getOrden();

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("      TALLER MECÁNICO - COMPROBANTE     \n");
        sb.append("========================================\n");
        sb.append("Factura N°:   ").append(f.getNumero()).append("\n");
        sb.append("Fecha:        ").append(sdf.format(f.getFechaEmision())).append("\n");
        sb.append("Cliente:      ").append(o.getCliente().getNombres()).append(" (DNI: ").append(o.getCliente().getDni()).append(")\n");
        sb.append("Vehículo:     ").append(o.getVehiculo().getPlaca()).append(" - ").append(o.getVehiculo().getColor()).append("\n");
        sb.append("Orden N°:     ").append(o.getCodigo()).append("\n");
        sb.append("Motivo:       ").append(o.getMotivoIngreso()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("DETALLE DE SERVICIOS Y REPUESTOS:\n");
        for (int i = 0; i < o.getCantidadItems(); i++) {
            ItemCotizacion item = o.getItems()[i];
            sb.append(String.format(" - %-20s x%-2d S/. %7.2f\n",
                    item.getReferencia().getNombre(),
                    item.getCantidad(),
                    item.calcularSubtotal()));
        }
        sb.append("----------------------------------------\n");
        sb.append(String.format("SUBTOTAL:                 S/. %10.2f\n", f.getSubtotal()));
        sb.append(String.format("IGV (18%%):                S/. %10.2f\n", f.getIgv()));
        sb.append(String.format("TOTAL A PAGAR:            S/. %10.2f\n", f.getTotal()));
        sb.append("========================================\n");
        sb.append("       ¡Gracias por su preferencia!     \n");

        JTextArea areaTicket = new JTextArea(sb.toString());
        areaTicket.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaTicket.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(areaTicket), "Ticket de Venta - " + f.getNumero(), JOptionPane.PLAIN_MESSAGE);
    }
}
