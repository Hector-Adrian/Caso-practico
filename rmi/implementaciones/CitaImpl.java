package rmi.implementaciones;

import rmi.ConexionMySQL;
import rmi.entidades.Cita;
import rmi.entidades.Medico;
import rmi.interfaces.CitaInterfaz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaImpl extends UnicastRemoteObject implements CitaInterfaz {

    public CitaImpl() throws RemoteException {
    }

    @Override
    public int crearCita(Cita cita) throws RemoteException {
        String sql = "INSERT INTO citas (fecha, hora, motivo, medico_id, paciente_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, new java.sql.Date(cita.getFecha().getTime()));
            ps.setString(2, cita.getHora());
            ps.setString(3, cita.getMotivo());
            ps.setInt(4, cita.getMedicoId());
            ps.setInt(5, cita.getPacienteId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al crear cita: " + e.getMessage());
            throw new RemoteException("Error al crear cita", e);
        }
        return -1;
    }

    @Override
    public List<Cita> obtenerTodasLasCitas() throws RemoteException {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.*, m.nombre AS medico_nombre, p.nombre AS paciente_nombre " +
                "FROM citas c " +
                "JOIN medicos m ON c.medico_id = m.id " +
                "JOIN pacientes p ON c.paciente_id = p.id";
        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getInt("id"),
                        rs.getDate("fecha"),
                        rs.getString("hora"),
                        rs.getString("motivo"),
                        rs.getInt("medico_id"),
                        rs.getInt("paciente_id")
                );
                cita.setMedicoNombre(rs.getString("medico_nombre"));
                cita.setPacienteNombre(rs.getString("paciente_nombre"));
                citas.add(cita);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las citas: " + e.getMessage());
            throw new RemoteException("Error al obtener todas las citas", e);
        }
        return citas;
    }

    @Override
    public boolean actualizarCita(Cita cita) throws RemoteException {
        String sql = "UPDATE citas SET fecha = ?, hora = ?, motivo = ?, medico_id = ?, paciente_id = ? WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(cita.getFecha().getTime()));
            ps.setString(2, cita.getHora());
            ps.setString(3, cita.getMotivo());
            ps.setInt(4, cita.getMedicoId());
            ps.setInt(5, cita.getPacienteId());
            ps.setInt(6, cita.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cita: " + e.getMessage());
            throw new RemoteException("Error al actualizar cita", e);
        }
    }

    @Override
    public boolean eliminarCita(int id) throws RemoteException {
        String sql = "DELETE FROM citas WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cita: " + e.getMessage());
            throw new RemoteException("Error al eliminar cita", e);
        }
    }

    @Override
    public List<Medico> buscarMedicosPorFecha(String especialidad) throws RemoteException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE especialidad LIKE ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + especialidad + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                medicos.add(new Medico(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        rs.getString("cedula"),
                        rs.getString("correo_electronico")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar médicos por especialidad (desde citas): " + e.getMessage());
            throw new RemoteException("Error al buscar médicos por especialidad", e);
        }
        return medicos;
    }
}