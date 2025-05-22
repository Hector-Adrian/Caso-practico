package rmi.entidades;

import java.io.Serializable;

public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String curp;
    private String telefono;
    private String correoElectronico;

    // Constructor vacío
    public Paciente() {}

    // Constructor completo
    public Paciente(String nombre, String curp, String telefono, String correoElectronico) {
        this.nombre = nombre;
        this.curp = curp;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    // Constructor con ID
    public Paciente(int id, String nombre, String curp, String telefono, String correoElectronico) {
        this.id = id;
        this.nombre = nombre;
        this.curp = curp;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", curp='" + curp + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                '}';
    }
}
