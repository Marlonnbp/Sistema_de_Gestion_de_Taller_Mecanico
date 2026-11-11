package modelo;

public class Cliente {
    private String dni;
    private String nombres;
    private String telefono;
    private String correo;
    private boolean activo;

    public Cliente(String dni, String nombres, String telefono, String correo) {
        this.dni = dni;
        this.nombres = nombres;
        this.telefono = telefono;
        this.correo = correo;
        this.activo = true; // Por defecto, al crear un cliente está activo
    }

    // Getters
    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setDni(String dni) { this.dni = dni; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}