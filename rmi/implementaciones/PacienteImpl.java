package rmi.implementaciones;

import rmi.entidades.Paciente;
import rmi.interfaces.PacienteInterfaz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PacienteImpl extends UnicastRemoteObject implements PacienteInterfaz {

    public PacienteImpl() throws RemoteException {
    }

    @Override
    public int crearPaciente(Paciente paciente) throws RemoteException {
        return 0;
    }

    @Override
    public List<Paciente> obtenerTodosPacientes() throws RemoteException {
        return List.of();
    }

    @Override
    public Paciente obtenerPacientePorId(int id) throws RemoteException {
        return null;
    }

    @Override
    public boolean actualizarPaciente(Paciente paciente) throws RemoteException {
        return false;
    }

    @Override
    public boolean eliminarPaciente(int id) throws RemoteException {
        return false;
    }

    @Override
    public Paciente buscarPacientePorCurp(String curp) throws RemoteException {
        return null;
    }
}
