package rmi;

import rmi.implementaciones.CitaImpl;
import rmi.implementaciones.MedicoImpl;
import rmi.implementaciones.PacienteImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServidorCrud {
    public static void main(String[] args) {

        try{
            CitaImpl cita = new CitaImpl();
            MedicoImpl medico = new MedicoImpl();
            PacienteImpl paciente = new PacienteImpl();

            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("Cita", cita);
            registry.rebind("Medico", medico);
            registry.rebind("Paciente", paciente);

            System.out.println("Servidor RMI listo...");

        } catch (Exception e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
