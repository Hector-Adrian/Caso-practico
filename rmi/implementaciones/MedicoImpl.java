package rmi.implementaciones;

import rmi.ConexionMySQL;
import rmi.entidades.Medico;
import rmi.interfaces.MedicoInterfaz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoImpl extends UnicastRemoteObject implements MedicoInterfaz {

    public MedicoImpl() throws RemoteException {
    }

    @Override
    public int crearMedico(Medico medico) throws RemoteException {
        String sql = "INSERT INTO medicos (nombre, especialidad, cedula, correo_electronico) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getEspecialidad());
            ps.setString(3, medico.getCedula());
            ps.setString(4, medico.getCorreoElectronico());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al crear médico: " + e.getMessage());
            throw new RemoteException("Error al crear médico", e);
        }
        return -1;
    }

    @Override
    public List<Medico> obtenerTodosMedicos() throws RemoteException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medicos";
        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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
            System.err.println("Error al obtener todos los médicos: " + e.getMessage());
            throw new RemoteException("Error al obtener todos los médicos", e);
        }
        return medicos;
    }

    @Override
    public boolean actualizarMedico(Medico medico) throws RemoteException {
        String sql = "UPDATE medicos SET nombre = ?, especialidad = ?, cedula = ?, correo_electronico = ? WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medico.getNombre());
            ps.setString(2, medico.getEspecialidad());
            ps.setString(3, medico.getCedula());
            ps.setString(4, medico.getCorreoElectronico());
            ps.setInt(5, medico.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar médico: " + e.getMessage());
            throw new RemoteException("Error al actualizar médico", e);
        }
    }

    @Override
    public boolean eliminarMedico(int id) throws RemoteException {
        String sql = "DELETE FROM medicos WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar médico: " + e.getMessage());
            throw new RemoteException("Error al eliminar médico", e);
        }
    }

    @Override
    public List<Medico> buscarMedicosPorEspecialidad(String especialidad) throws RemoteException {
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
            System.err.println("Error al buscar médicos por especialidad: " + e.getMessage());
            throw new RemoteException("Error al buscar médicos por especialidad", e);
        }
        return medicos;
    }
}