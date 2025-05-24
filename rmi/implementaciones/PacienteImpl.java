package rmi.implementaciones;

import rmi.ConexionMySQL;
import rmi.entidades.Paciente;
import rmi.interfaces.PacienteInterfaz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteImpl extends UnicastRemoteObject implements PacienteInterfaz {

    public PacienteImpl() throws RemoteException {
    }

    @Override
    public int crearPaciente(Paciente paciente) throws RemoteException {
        String sql = "INSERT INTO pacientes (nombre, curp, telefono, correo_electronico) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getCurp());
            ps.setString(3, paciente.getTelefono());
            ps.setString(4, paciente.getCorreoElectronico());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al crear paciente: " + e.getMessage());
            throw new RemoteException("Error al crear paciente", e);
        }
        return -1;
    }

    @Override
    public List<Paciente> obtenerTodosPacientes() throws RemoteException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM pacientes";
        try (Connection conn = ConexionMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pacientes.add(new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("curp"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pacientes: " + e.getMessage());
            throw new RemoteException("Error al obtener todos los pacientes", e);
        }
        return pacientes;
    }

    @Override
    public Paciente obtenerPacientePorId(int id) throws RemoteException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("curp"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener paciente por ID: " + e.getMessage());
            throw new RemoteException("Error al obtener paciente por ID", e);
        }
        return null;
    }

    @Override
    public boolean actualizarPaciente(Paciente paciente) throws RemoteException {
        String sql = "UPDATE pacientes SET nombre = ?, curp = ?, telefono = ?, correo_electronico = ? WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paciente.getNombre());
            ps.setString(2, paciente.getCurp());
            ps.setString(3, paciente.getTelefono());
            ps.setString(4, paciente.getCorreoElectronico());
            ps.setInt(5, paciente.getId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            throw new RemoteException("Error al actualizar paciente", e);
        }
    }

    @Override
    public boolean eliminarPaciente(int id) throws RemoteException {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            throw new RemoteException("Error al eliminar paciente", e);
        }
    }

    @Override
    public Paciente buscarPacientePorCurp(String curp) throws RemoteException {
        String sql = "SELECT * FROM pacientes WHERE curp = ?";
        try (Connection conn = ConexionMySQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, curp);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Paciente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("curp"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar paciente por CURP: " + e.getMessage());
            throw new RemoteException("Error al buscar paciente por CURP", e);
        }
        return null;
    }
}