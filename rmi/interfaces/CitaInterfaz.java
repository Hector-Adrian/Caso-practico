package rmi.interfaces;

import rmi.entidades.Cita;
import rmi.entidades.Medico;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CitaInterfaz extends Remote {

    int crearCita(Cita cita) throws RemoteException;


    List<Cita> obtenerTodasLasCitas() throws RemoteException;


    boolean actualizarCita(Cita cita) throws RemoteException;


    boolean eliminarCita(int id) throws RemoteException;



    List<Medico> buscarMedicosPorFecha(String especialidad) throws RemoteException;
}
