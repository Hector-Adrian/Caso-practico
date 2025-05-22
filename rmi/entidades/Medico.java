
package rmi.entidades;

import java.io.Serializable;

public class Medico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String especialidad;
    private String cedula;
    private String correoElectronico;

    // Constructor vacío
    public Medico() {}

    // Constructor completo
    public Medico(String nombre, String especialidad, String cedula, String correoElectronico) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
    }

    // Constructor con ID
    public Medico(int id, String nombre, String especialidad, String cedula, String correoElectronico) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.cedula = cedula;
        this.correoElectronico = correoElectronico;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", cedula='" + cedula + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                '}';
    }
}