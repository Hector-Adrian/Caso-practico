package rmi.interfaces;

import rmi.entidades.Paciente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PacienteInterfaz extends Remote {


    int crearPaciente(Paciente paciente) throws RemoteException;

    boolean actualizarPaciente(Paciente paciente) throws RemoteException;

    boolean eliminarPaciente(int id) throws RemoteException;


    Paciente buscarPacientePorCurp(String curp) throws RemoteException;


    Paciente obtenerPacientePorId(int id) throws RemoteException;


    List<Paciente> obtenerTodosPacientes() throws RemoteException;

}
