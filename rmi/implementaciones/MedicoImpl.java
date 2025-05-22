package rmi.implementaciones;

import rmi.entidades.Medico;
import rmi.interfaces.MedicoInterfaz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MedicoImpl extends UnicastRemoteObject implements MedicoInterfaz {

    public MedicoImpl() throws RemoteException {
    }

    @Override
    public int crearMedico(Medico medico) throws RemoteException {
        return 0;
    }

    @Override
    public List<Medico> obtenerTodosMedicos() throws RemoteException {
        return List.of();
    }

    @Override
    public boolean actualizarMedico(Medico medico) throws RemoteException {
        return false;
    }

    @Override
    public boolean eliminarMedico(int id) throws RemoteException {
        return false;
    }

    @Override
    public List<Medico> buscarMedicosPorEspecialidad(String especialidad) throws RemoteException {
        return List.of();
    }
}
