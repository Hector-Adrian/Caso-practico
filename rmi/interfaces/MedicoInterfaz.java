package rmi.interfaces;

import rmi.entidades.Medico;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MedicoInterfaz extends Remote {
    

    int crearMedico(Medico medico) throws RemoteException;
    

    List<Medico> obtenerTodosMedicos() throws RemoteException;
    


    boolean actualizarMedico(Medico medico) throws RemoteException;
    

    boolean eliminarMedico(int id) throws RemoteException;
    


    List<Medico> buscarMedicosPorEspecialidad(String especialidad) throws RemoteException;
}
