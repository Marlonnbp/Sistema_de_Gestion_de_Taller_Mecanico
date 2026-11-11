package persistencia;

import gestores.Catalogo;
import gestores.GestorClientes;
import gestores.GestorOrdenes;
import gestores.GestorVehiculos;
import gestores.HistorialFacturas;
import modelo.Cliente;
import modelo.EstadoOrden;
import modelo.Factura;
import modelo.ItemCatalogo;
import modelo.ItemCotizacion;
import modelo.OrdenTrabajo;
import modelo.Vehiculo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GestorArchivos {

    private static final String RUTA_CLIENTES = "datos/clientes.txt";
    private static final String RUTA_CATALOGO = "datos/catalogo.txt";
    private static final String RUTA_VEHICULOS = "datos/vehiculos.txt";
    private static final String RUTA_ORDENES = "datos/ordenes.txt";
    private static final String RUTA_FACTURAS = "datos/facturas.txt";

    // --------------------------------------------------------
    // MÓDULO CLIENTES
    // --------------------------------------------------------
    public static void guardarClientes(GestorClientes gestor) {
        Cliente[] clientes = gestor.listarTodos(); 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_CLIENTES))) {
            for (Cliente c : clientes) {
                if (c != null) {
                    String linea = c.getDni() + ";" + c.getNombres() + ";" + c.getTelefono() + ";" + c.getCorreo() + ";" + c.isActivo();
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
        }
    }

    public static void cargarClientes(GestorClientes gestor) {
        File archivo = new File(RUTA_CLIENTES);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length == 5) {
                    Cliente c = new Cliente(p[0], p[1], p[2], p[3]);
                    c.setActivo(Boolean.parseBoolean(p[4]));
                    gestor.crear(c); 
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // MÓDULO CATÁLOGO
    // --------------------------------------------------------
    public static void guardarCatalogo(Catalogo gestor) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_CATALOGO))) {
            String[] lineas = gestor.toLineas();
            for (String linea : lineas) {
                if (linea != null) {
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar catálogo: " + e.getMessage());
        }
    }

    public static void cargarCatalogo(Catalogo gestor) {
        File archivo = new File(RUTA_CATALOGO);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                gestor.cargarDesdeLinea(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar catálogo: " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // MÓDULO VEHÍCULOS
    // --------------------------------------------------------
    public static void guardarVehiculos(GestorVehiculos gestor) {
        Vehiculo[] vehiculos = gestor.listarTodos();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_VEHICULOS))) {
            for (Vehiculo v : vehiculos) {
                if (v != null) {
                    String linea = v.getPlaca() + ";" + v.getAnio() + ";" + v.getColor() + ";" + v.getKilometraje() + ";" + v.getPropietario().getDni() + ";" + v.isActivo();
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar vehículos: " + e.getMessage());
        }
    }

    public static void cargarVehiculos(GestorVehiculos gestorVehiculos, GestorClientes gestorClientes) {
        File archivo = new File(RUTA_VEHICULOS);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length == 6) {
                    Cliente propietario = gestorClientes.leerCualquiera(p[4]); 
                    if (propietario != null) {
                        Vehiculo v = new Vehiculo(p[0], Integer.parseInt(p[1]), p[2], Integer.parseInt(p[3]), propietario);
                        v.setActivo(Boolean.parseBoolean(p[5]));
                        gestorVehiculos.crear(v);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar vehículos: " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // MÓDULO ÓRDENES DE TRABAJO
    // --------------------------------------------------------
    public static void guardarOrdenes(GestorOrdenes gestor) {
        OrdenTrabajo[] ordenes = gestor.listarTodos(); 
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_ORDENES))) {
            for (OrdenTrabajo o : ordenes) {
                if (o != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(o.getCodigo()).append(";")
                      .append(o.getFechaIngreso().getTime()).append(";")
                      .append(o.getMotivoIngreso()).append(";")
                      .append(o.getEstado().name()).append(";")
                      .append(o.getCliente().getDni()).append(";")
                      .append(o.getVehiculo().getPlaca()).append(";");
                    
                    ItemCotizacion[] items = o.getItems();
                    StringBuilder sbItems = new StringBuilder();
                    for (int i = 0; i < o.getCantidadItems(); i++) {
                        sbItems.append(items[i].getReferencia().getCodigo()).append(",")
                               .append(items[i].getCantidad()).append(",")
                               .append(items[i].getPrecioUnitario());
                        if (i < o.getCantidadItems() - 1) sbItems.append("|");
                    }
                    sb.append(sbItems.toString());
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar órdenes: " + e.getMessage());
        }
    }

    public static void cargarOrdenes(GestorOrdenes gestor, GestorClientes gClientes, GestorVehiculos gVehiculos, Catalogo catalogo) {
        File archivo = new File(RUTA_ORDENES);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", -1); 
                if (p.length >= 7) {
                    Cliente c = gClientes.leerCualquiera(p[4]);
                    Vehiculo v = gVehiculos.leerCualquiera(p[5]);
                    
                    if (c != null && v != null) {
                        OrdenTrabajo o = new OrdenTrabajo(p[0], p[2], c, v);
                        o.setFechaIngreso(new java.util.Date(Long.parseLong(p[1])));
                        o.cambiarEstado(EstadoOrden.valueOf(p[3]));
                        
                        if (!p[6].isEmpty()) {
                            String[] arrayItems = p[6].split("\\|");
                            for (String it : arrayItems) {
                                String[] datosItem = it.split(",");
                                ItemCatalogo ref = catalogo.leerCualquiera(datosItem[0]);
                                if (ref != null) {
                                    o.cargarItemHistorico(ref, Integer.parseInt(datosItem[1]), Double.parseDouble(datosItem[2]));
                                }
                            }
                        }
                        gestor.agregarDesdeArchivo(o);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar órdenes: " + e.getMessage());
        }
    }

    // --------------------------------------------------------
    // MÓDULO FACTURAS
    // --------------------------------------------------------
    public static void guardarFacturas(HistorialFacturas gestor) {
        Factura[] facturas = gestor.listarTodos();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA_FACTURAS))) {
            for (Factura f : facturas) {
                if (f != null) {
                    String linea = f.getNumero() + ";" + f.getFechaEmision().getTime() + ";" + f.getSubtotal() + ";" + f.getIgv() + ";" + f.getTotal() + ";" + f.getOrden().getCodigo();
                    bw.write(linea);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error al guardar facturas: " + e.getMessage());
        }
    }

    public static void cargarFacturas(HistorialFacturas gestor, GestorOrdenes gOrdenes) {
        File archivo = new File(RUTA_FACTURAS);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                if (p.length == 6) {
                    OrdenTrabajo orden = gOrdenes.leerCualquiera(p[5]);
                    if (orden != null) {
                        Factura f = new Factura(p[0], new java.util.Date(Long.parseLong(p[1])), Double.parseDouble(p[2]), Double.parseDouble(p[3]), Double.parseDouble(p[4]), orden);
                        gestor.agregarDesdeArchivo(f);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar facturas: " + e.getMessage());
        }
    }
}