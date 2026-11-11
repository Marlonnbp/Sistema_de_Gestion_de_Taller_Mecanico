package modelo;

public class Vehiculo {
    private String placa;
    private int anio;
    private String color;
    private int kilometraje;
    private Cliente propietario;
    private boolean activo; // Agregado para la baja lógica

    public Vehiculo(String placa, int anio, String color, int kilometraje, Cliente propietario) {
        this.placa = placa;
        this.anio = anio;
        this.color = color;
        this.kilometraje = kilometraje;
        this.propietario = propietario;
        this.activo = true; // Por defecto nace activo
    }

    public void actualizarKilometraje(int km) {
        if (km > this.kilometraje) {
            this.kilometraje = km;
        }
    }

    // Getters
    public String getPlaca() { return placa; }
    public int getAnio() { return anio; }
    public String getColor() { return color; }
    public int getKilometraje() { return kilometraje; }
    public Cliente getPropietario() { return propietario; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setPlaca(String placa) { this.placa = placa; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setColor(String color) { this.color = color; }
    public void setKilometraje(int kilometraje) { this.kilometraje = kilometraje; }
    public void setPropietario(Cliente propietario) { this.propietario = propietario; }
    public void setActivo(boolean activo) { this.activo = activo; }
}