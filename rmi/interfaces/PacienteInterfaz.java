package rmi.interfaces;


import rmi.entidades.Paciente;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PacienteInterfaz extends Remote {
    
    /**
     * Crear un nuevo paciente
     * @param paciente Objeto Paciente a crear
     * @return ID del paciente creado
     * @throws RemoteException
     */
    int crearPaciente(Paciente paciente) throws RemoteException;
    
    /**
     * Obtener todos los pacientes
     * @return Lista de pacientes
     * @throws RemoteException
     */
    List<Paciente> obtenerTodosPacientes() throws RemoteException;
    
    /**
     * Obtener un paciente por ID
     * @param id ID del paciente
     * @return Objeto Paciente o null si no existe
     * @throws RemoteException
     */
    Paciente obtenerPacientePorId(int id) throws RemoteException;
    
    /**
     * Actualizar un paciente existente
     * @param paciente Objeto Paciente con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     * @throws RemoteException
     */
    boolean actualizarPaciente(Paciente paciente) throws RemoteException;
    
    /**
     * Eliminar un paciente
     * @param id ID del paciente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     * @throws RemoteException
     */
    boolean eliminarPaciente(int id) throws RemoteException;
    
    /**
     * Buscar paciente por CURP
     * @param curp CURP del paciente
     * @return Objeto Paciente o null si no existe
     * @throws RemoteException
     */
    Paciente buscarPacientePorCurp(String curp) throws RemoteException;
}
