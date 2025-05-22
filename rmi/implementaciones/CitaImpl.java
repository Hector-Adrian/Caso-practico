package rmi.implementaciones;

import rmi.entidades.Cita;
import rmi.entidades.Medico;
import rmi.interfaces.CitaInterfaz;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class CitaImpl extends UnicastRemoteObject  implements CitaInterfaz {

    public CitaImpl() throws RemoteException {
    }

    @Override
    public int crearCita(Cita cita) throws RemoteException {
        return 0;
    }

    @Override
    public List<Cita> obtenerTodasLasCitas() throws RemoteException {
        return List.of();
    }

    @Override
    public boolean actualizarCita(Cita cita) throws RemoteException {
        return false;
    }

    @Override
    public boolean eliminarCita(int id) throws RemoteException {
        return false;
    }

    @Override
    public List<Medico> buscarMedicosPorFecha(String especialidad) throws RemoteException {
        return List.of();
    }
}
