package rmi.entidades;

import java.io.Serializable;
import java.util.Date;

public class Cita implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private Date fecha;
    private String hora;
    private String motivo;
    private int medicoId;
    private int pacienteId;
    
    // Para mostrar información completa
    private String medicoNombre;
    private String pacienteNombre;
    
    // Constructor vacío
    public Cita() {}
    
    // Constructor completo
    public Cita(Date fecha, String hora, String motivo, int medicoId, int pacienteId) {
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
    }
    
    // Constructor con ID
    public Cita(int id, Date fecha, String hora, String motivo, int medicoId, int pacienteId) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }
    
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    
    public String getMedicoNombre() { return medicoNombre; }
    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }
    
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    
    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", hora='" + hora + '\'' +
                ", motivo='" + motivo + '\'' +
                ", medicoId=" + medicoId +
                ", pacienteId=" + pacienteId +
                ", medicoNombre='" + medicoNombre + '\'' +
                ", pacienteNombre='" + pacienteNombre + '\'' +
                '}';
    }
}