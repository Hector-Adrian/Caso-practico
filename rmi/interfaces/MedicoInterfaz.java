package rmi.interfaces;

import rmi.entidades.Medico;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MedicoInterfaz extends Remote {
    
    /**
     * Crear un nuevo médico
     * @param medico Objeto Medico a crear
     * @return ID del médico creado
     * @throws RemoteException
     */
    int crearMedico(Medico medico) throws RemoteException;
    
    /**
     * Obtener todos los médicos
     * @return Lista de médicos
     * @throws RemoteException
     */
    List<Medico> obtenerTodosMedicos() throws RemoteException;
    
    /**
     * Obtener un médico por ID
     * @param id ID del médico
     * @return Objeto Medico o null si no existe
     * @throws RemoteException
     */
    Medico obtenerMedicoPorId(int id) throws RemoteException;
    
    /**
     * Actualizar un médico existente
     * @param medico Objeto Medico con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     * @throws RemoteException
     */
    boolean actualizarMedico(Medico medico) throws RemoteException;
    
    /**
     * Eliminar un médico
     * @param id ID del médico a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws RemoteException
     */
    boolean eliminarMedico(int id) throws RemoteException;
    
    /**
     * Buscar médicos por especialidad
     * @param especialidad Especialidad a buscar
     * @return Lista de médicos con esa especialidad
     * @throws RemoteException
     */
    List<Medico> buscarMedicosPorEspecialidad(String especialidad) throws RemoteException;
}
