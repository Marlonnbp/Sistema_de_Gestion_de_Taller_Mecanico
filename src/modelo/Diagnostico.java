package modelo;

import java.util.Date;

public class Diagnostico {
    private String descripcion;
    private Date fechaRegistro;

    // Usamos el constructor para hacer el registro inicial
    public Diagnostico(String descripcion) {
        registrar(descripcion);
    }

    // Cumple con el método + registrar(descripcion: String): void del diagrama
    public void registrar(String descripcion) {
        this.descripcion = descripcion;
        this.fechaRegistro = new Date(); // Toma la fecha y hora exacta del registro
    }

    // Cumple con el método + consultar(): String del diagrama
    public String consultar() {
        return this.descripcion;
    }

    // Cumple con el método + actualizar(descripcion: String): void del diagrama
    public void actualizar(String descripcion) {
        this.descripcion = descripcion;
        this.fechaRegistro = new Date(); // Actualiza la fecha a la de la última modificación
    }

    // Getters convencionales extra (muy útiles cuando toque guardar en archivos)
    public String getDescripcion() {
        return descripcion;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }
}