// Caso-practico-main/rmi/Cliente.java
package rmi;

import rmi.entidades.Cita;
import rmi.entidades.Medico;
import rmi.entidades.Paciente;
import rmi.interfaces.CitaInterfaz;
import rmi.interfaces.MedicoInterfaz;
import rmi.interfaces.PacienteInterfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cliente extends JFrame {

    private CitaInterfaz citaService;
    private MedicoInterfaz medicoService;
    private PacienteInterfaz pacienteService;

    // Paneles y Tabs
    private JTabbedPane tabbedPane;

    // Componentes para Pacientes
    private JTextField txtPacienteId, txtPacienteNombre, txtPacienteCurp, txtPacienteTelefono, txtPacienteCorreo;
    private JButton btnPacienteCrear, btnPacienteActualizar, btnPacienteEliminar, btnPacienteBuscarCurp, btnPacienteLimpiar;
    private JTable tblPacientes;
    private DefaultTableModel modelPacientes;

    // Componentes para Medicos
    private JTextField txtMedicoId, txtMedicoNombre, txtMedicoEspecialidad, txtMedicoCedula, txtMedicoCorreo;
    private JButton btnMedicoCrear, btnMedicoActualizar, btnMedicoEliminar, btnMedicoBuscarEspecialidad, btnMedicoLimpiar;
    private JTable tblMedicos;
    private DefaultTableModel modelMedicos;

    // Componentes para Citas - REVERTIDOS A JTextField
    private JTextField txtCitaId, txtCitaFecha, txtCitaHora, txtCitaMotivo, txtCitaMedicoId, txtCitaPacienteId;
    private JButton btnCitaCrear, btnCitaActualizar, btnCitaEliminar, btnCitaBuscarMedicosFecha, btnCitaLimpiar;
    private JTable tblCitas;
    private DefaultTableModel modelCitas;

    // Expresiones regulares para validación
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String CURP_REGEX = "^[A-Z]{4}[0-9]{6}[H,M][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9][0-9]$"; // CURP corregida
    private static final String TELEFONO_REGEX = "^[0-9]{10}$"; // Asumiendo números de teléfono de 10 dígitos
    private static final String HORA_REGEX = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$"; // Formato HH:MM (24 horas)

    public Cliente() {
        super("Sistema de Gestión de Citas Médicas RMI");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar servicios RMI
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            citaService = (CitaInterfaz) registry.lookup("Cita");
            medicoService = (MedicoInterfaz) registry.lookup("Medico");
            pacienteService = (PacienteInterfaz) registry.lookup("Paciente");
            System.out.println("Conexión RMI exitosa.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor RMI: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        tabbedPane = new JTabbedPane();
        add(tabbedPane);

        // Inicializar y agregar paneles
        setupPacientePanel();
        setupMedicoPanel();
        setupCitaPanel();

        setVisible(true);
    }

    private void setupPacientePanel() {
        JPanel pacientePanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));

        formPanel.add(new JLabel("ID:"));
        txtPacienteId = new JTextField();
        txtPacienteId.setEditable(false);
        formPanel.add(txtPacienteId);

        formPanel.add(new JLabel("Nombre:"));
        txtPacienteNombre = new JTextField();
        formPanel.add(txtPacienteNombre);

        formPanel.add(new JLabel("CURP:"));
        txtPacienteCurp = new JTextField();
        formPanel.add(txtPacienteCurp);

        formPanel.add(new JLabel("Teléfono:"));
        txtPacienteTelefono = new JTextField();
        formPanel.add(txtPacienteTelefono);

        formPanel.add(new JLabel("Correo Electrónico:"));
        txtPacienteCorreo = new JTextField();
        formPanel.add(txtPacienteCorreo);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnPacienteCrear = new JButton("Crear");
        btnPacienteActualizar = new JButton("Actualizar");
        btnPacienteEliminar = new JButton("Eliminar");
        btnPacienteBuscarCurp = new JButton("Buscar por CURP");
        btnPacienteLimpiar = new JButton("Limpiar Campos");

        buttonPanel.add(btnPacienteCrear);
        buttonPanel.add(btnPacienteActualizar);
        buttonPanel.add(btnPacienteEliminar);
        buttonPanel.add(btnPacienteBuscarCurp);
        buttonPanel.add(btnPacienteLimpiar);

        pacientePanel.add(formPanel, BorderLayout.NORTH);
        pacientePanel.add(buttonPanel, BorderLayout.CENTER);

        // Tabla de pacientes
        String[] columnNames = {"ID", "Nombre", "CURP", "Teléfono", "Correo Electrónico"};
        modelPacientes = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPacientes = new JTable(modelPacientes);
        tblPacientes.getTableHeader().setReorderingAllowed(false); // Evitar reordenamiento de columnas
        JScrollPane scrollPane = new JScrollPane(tblPacientes);
        pacientePanel.add(scrollPane, BorderLayout.SOUTH);

        tabbedPane.addTab("Pacientes", pacientePanel);

        // Listeners
        btnPacienteCrear.addActionListener(e -> crearPaciente());
        btnPacienteActualizar.addActionListener(e -> actualizarPaciente());
        btnPacienteEliminar.addActionListener(e -> eliminarPaciente());
        btnPacienteBuscarCurp.addActionListener(e -> buscarPacientePorCurp());
        btnPacienteLimpiar.addActionListener(e -> limpiarCamposPaciente());
        tblPacientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPacientes.getSelectedRow() != -1) {
                cargarPacienteSeleccionado();
            }
        });

        cargarTodosPacientes();
    }

    private void setupMedicoPanel() {
        JPanel medicoPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos del Médico"));

        formPanel.add(new JLabel("ID:"));
        txtMedicoId = new JTextField();
        txtMedicoId.setEditable(false);
        formPanel.add(txtMedicoId);

        formPanel.add(new JLabel("Nombre:"));
        txtMedicoNombre = new JTextField();
        formPanel.add(txtMedicoNombre);

        formPanel.add(new JLabel("Especialidad:"));
        txtMedicoEspecialidad = new JTextField();
        formPanel.add(txtMedicoEspecialidad);

        formPanel.add(new JLabel("Cédula:"));
        txtMedicoCedula = new JTextField();
        formPanel.add(txtMedicoCedula);

        formPanel.add(new JLabel("Correo Electrónico:"));
        txtMedicoCorreo = new JTextField();
        formPanel.add(txtMedicoCorreo);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnMedicoCrear = new JButton("Crear");
        btnMedicoActualizar = new JButton("Actualizar");
        btnMedicoEliminar = new JButton("Eliminar");
        btnMedicoBuscarEspecialidad = new JButton("Buscar por Especialidad");
        btnMedicoLimpiar = new JButton("Limpiar Campos");

        buttonPanel.add(btnMedicoCrear);
        buttonPanel.add(btnMedicoActualizar);
        buttonPanel.add(btnMedicoEliminar);
        buttonPanel.add(btnMedicoBuscarEspecialidad);
        buttonPanel.add(btnMedicoLimpiar);

        medicoPanel.add(formPanel, BorderLayout.NORTH);
        medicoPanel.add(buttonPanel, BorderLayout.CENTER);

        // Tabla de médicos
        String[] columnNames = {"ID", "Nombre", "Especialidad", "Cédula", "Correo Electrónico"};
        modelMedicos = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMedicos = new JTable(modelMedicos);
        tblMedicos.getTableHeader().setReorderingAllowed(false); // Evitar reordenamiento de columnas
        JScrollPane scrollPane = new JScrollPane(tblMedicos);
        medicoPanel.add(scrollPane, BorderLayout.SOUTH);

        tabbedPane.addTab("Médicos", medicoPanel);

        // Listeners
        btnMedicoCrear.addActionListener(e -> crearMedico());
        btnMedicoActualizar.addActionListener(e -> actualizarMedico());
        btnMedicoEliminar.addActionListener(e -> eliminarMedico());
        btnMedicoBuscarEspecialidad.addActionListener(e -> buscarMedicosPorEspecialidad());
        btnMedicoLimpiar.addActionListener(e -> limpiarCamposMedico());
        tblMedicos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblMedicos.getSelectedRow() != -1) {
                cargarMedicoSeleccionado();
            }
        });

        cargarTodosMedicos();
    }

    private void setupCitaPanel() {
        JPanel citaPanel = new JPanel(new BorderLayout()); // Panel principal de la pestaña Citas

        // Panel superior para los campos de formulario y botones
        JPanel topPanel = new JPanel(new BorderLayout());

        // Panel de formulario con GridLayout
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5)); // 6 filas para los 6 campos
        formPanel.setBorder(BorderFactory.createTitledBorder("Datos de la Cita"));

        Dimension textFieldSize = new Dimension(250, 25); // Tamaño preferido para los JTextField

        formPanel.add(new JLabel("ID:"));
        txtCitaId = new JTextField();
        txtCitaId.setEditable(false);
        txtCitaId.setPreferredSize(textFieldSize);
        formPanel.add(txtCitaId);

        formPanel.add(new JLabel("Fecha (DD-MM-YYYY):")); // Etiqueta actualizada
        txtCitaFecha = new JTextField();
        txtCitaFecha.setPreferredSize(textFieldSize);
        formPanel.add(txtCitaFecha);

        formPanel.add(new JLabel("Hora (HH:MM):")); // Etiqueta que ya indica 24 horas
        txtCitaHora = new JTextField();
        txtCitaHora.setPreferredSize(textFieldSize);
        formPanel.add(txtCitaHora);

        formPanel.add(new JLabel("Motivo:"));
        txtCitaMotivo = new JTextField();
        txtCitaMotivo.setPreferredSize(textFieldSize);
        formPanel.add(txtCitaMotivo);

        formPanel.add(new JLabel("ID Médico:"));
        txtCitaMedicoId = new JTextField();
        txtCitaMedicoId.setPreferredSize(textFieldSize);
        formPanel.add(txtCitaMedicoId);

        formPanel.add(new JLabel("ID Paciente:"));
        txtCitaPacienteId = new JTextField();
        txtCitaPacienteId.setPreferredSize(textFieldSize);
        formPanel.add(txtCitaPacienteId);

        // Panel de botones con FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Centrar y añadir espacio
        btnCitaCrear = new JButton("Crear");
        btnCitaActualizar = new JButton("Actualizar");
        btnCitaEliminar = new JButton("Eliminar");
        btnCitaBuscarMedicosFecha = new JButton("Buscar Médicos por Especialidad");
        btnCitaLimpiar = new JButton("Limpiar Campos");

        buttonPanel.add(btnCitaCrear);
        buttonPanel.add(btnCitaActualizar);
        buttonPanel.add(btnCitaEliminar);
        buttonPanel.add(btnCitaBuscarMedicosFecha);
        buttonPanel.add(btnCitaLimpiar);

        topPanel.add(formPanel, BorderLayout.NORTH); // Formulario en la parte superior del topPanel
        topPanel.add(buttonPanel, BorderLayout.CENTER); // Botones en el centro del topPanel

        citaPanel.add(topPanel, BorderLayout.NORTH); // topPanel en la parte superior del citaPanel

        // Tabla de citas en el centro del citaPanel para que ocupe el espacio restante
        String[] columnNames = {"ID", "Fecha", "Hora", "Motivo", "ID Médico", "ID Paciente", "Nombre Médico", "Nombre Paciente"};
        modelCitas = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCitas = new JTable(modelCitas);
        tblCitas.getTableHeader().setReorderingAllowed(false); // Evitar reordenamiento de columnas
        tblCitas.setFillsViewportHeight(true); // Para que la tabla ocupe todo el espacio disponible
        JScrollPane scrollPane = new JScrollPane(tblCitas);
        citaPanel.add(scrollPane, BorderLayout.CENTER); // Tabla en la región central del citaPanel

        tabbedPane.addTab("Citas", citaPanel);

        // Listeners
        btnCitaCrear.addActionListener(e -> crearCita());
        btnCitaActualizar.addActionListener(e -> actualizarCita());
        btnCitaEliminar.addActionListener(e -> eliminarCita());
        btnCitaBuscarMedicosFecha.addActionListener(e -> buscarMedicosPorEspecialidadParaCita());
        btnCitaLimpiar.addActionListener(e -> limpiarCamposCita());
        tblCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblCitas.getSelectedRow() != -1) {
                cargarCitaSeleccionada();
            }
        });

        cargarTodasLasCitas();
    }


    // --- Métodos para Pacientes ---

    private void crearPaciente() {
        if (!validarCamposPaciente(false)) {
            return;
        }

        String nombre = txtPacienteNombre.getText();
        String curp = txtPacienteCurp.getText();
        String telefono = txtPacienteTelefono.getText();
        String correo = txtPacienteCorreo.getText();

        Paciente paciente = new Paciente(nombre, curp, telefono, correo);
        try {
            int id = pacienteService.crearPaciente(paciente);
            JOptionPane.showMessageDialog(this, "Paciente creado con ID: " + id);
            limpiarCamposPaciente();
            cargarTodosPacientes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarPaciente() {
        if (!validarCamposPaciente(true)) {
            return;
        }

        try {
            int id = Integer.parseInt(txtPacienteId.getText());
            String nombre = txtPacienteNombre.getText();
            String curp = txtPacienteCurp.getText();
            String telefono = txtPacienteTelefono.getText();
            String correo = txtPacienteCorreo.getText();

            Paciente paciente = new Paciente(id, nombre, curp, telefono, correo);
            boolean actualizado = pacienteService.actualizarPaciente(paciente);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Paciente actualizado exitosamente.");
                limpiarCamposPaciente();
                cargarTodosPacientes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de paciente inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaciente() {
        String idText = txtPacienteId.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente de la tabla o ingrese un ID para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este paciente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idText);
                boolean eliminado = pacienteService.eliminarPaciente(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Paciente eliminado exitosamente.");
                    limpiarCamposPaciente();
                    cargarTodosPacientes();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el paciente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de paciente inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar paciente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarPacientePorCurp() {
        String curp = txtPacienteCurp.getText().trim();
        if (curp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una CURP para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!curp.matches(CURP_REGEX)) {
            JOptionPane.showMessageDialog(this, "Formato de CURP incorrecto (ej. ABCD123456HABCDE01).", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Paciente paciente = pacienteService.buscarPacientePorCurp(curp);
            if (paciente != null) {
                modelPacientes.setRowCount(0); // Limpiar tabla
                modelPacientes.addRow(new Object[]{paciente.getId(), paciente.getNombre(), paciente.getCurp(), paciente.getTelefono(), paciente.getCorreoElectronico()});
                cargarPacienteEnFormulario(paciente);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún paciente con la CURP proporcionada.", "No Encontrado", JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposPaciente();
                cargarTodosPacientes(); // Recargar todos para mostrar la lista original
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar paciente por CURP: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTodosPacientes() {
        modelPacientes.setRowCount(0); // Limpiar tabla
        try {
            List<Paciente> pacientes = pacienteService.obtenerTodosPacientes();
            for (Paciente p : pacientes) {
                modelPacientes.addRow(new Object[]{p.getId(), p.getNombre(), p.getCurp(), p.getTelefono(), p.getCorreoElectronico()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPacienteSeleccionado() {
        int selectedRow = tblPacientes.getSelectedRow();
        if (selectedRow >= 0) {
            txtPacienteId.setText(modelPacientes.getValueAt(selectedRow, 0).toString());
            txtPacienteNombre.setText(modelPacientes.getValueAt(selectedRow, 1).toString());
            txtPacienteCurp.setText(modelPacientes.getValueAt(selectedRow, 2).toString());
            txtPacienteTelefono.setText(modelPacientes.getValueAt(selectedRow, 3).toString());
            txtPacienteCorreo.setText(modelPacientes.getValueAt(selectedRow, 4).toString());
        }
    }

    private void cargarPacienteEnFormulario(Paciente paciente) {
        txtPacienteId.setText(String.valueOf(paciente.getId()));
        txtPacienteNombre.setText(paciente.getNombre());
        txtPacienteCurp.setText(paciente.getCurp());
        txtPacienteTelefono.setText(paciente.getTelefono());
        txtPacienteCorreo.setText(paciente.getCorreoElectronico());
    }

    private void limpiarCamposPaciente() {
        txtPacienteId.setText("");
        txtPacienteNombre.setText("");
        txtPacienteCurp.setText("");
        txtPacienteTelefono.setText("");
        txtPacienteCorreo.setText("");
        cargarTodosPacientes();
    }

    private boolean validarCamposPaciente(boolean isUpdate) {
        if (isUpdate && txtPacienteId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente o ingrese un ID para actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtPacienteNombre.getText().trim().isEmpty() ||
                txtPacienteCurp.getText().trim().isEmpty() ||
                txtPacienteTelefono.getText().trim().isEmpty() ||
                txtPacienteCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!txtPacienteCurp.getText().trim().matches(CURP_REGEX)) {
            JOptionPane.showMessageDialog(this, "Formato de CURP incorrecto (ej. ABCD123456HABCDE01).", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtPacienteTelefono.getText().trim().matches(TELEFONO_REGEX)) {
            JOptionPane.showMessageDialog(this, "Formato de Teléfono incorrecto (10 dígitos numéricos).", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!txtPacienteCorreo.getText().trim().matches(EMAIL_REGEX)) {
            JOptionPane.showMessageDialog(this, "Formato de Correo Electrónico incorrecto.", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // --- Métodos para Médicos ---

    private void crearMedico() {
        if (!validarCamposMedico(false)) {
            return;
        }

        String nombre = txtMedicoNombre.getText();
        String especialidad = txtMedicoEspecialidad.getText();
        String cedula = txtMedicoCedula.getText();
        String correo = txtMedicoCorreo.getText();

        Medico medico = new Medico(nombre, especialidad, cedula, correo);
        try {
            int id = medicoService.crearMedico(medico);
            JOptionPane.showMessageDialog(this, "Médico creado con ID: " + id);
            limpiarCamposMedico();
            cargarTodosMedicos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarMedico() {
        if (!validarCamposMedico(true)) {
            return;
        }

        try {
            int id = Integer.parseInt(txtMedicoId.getText());
            String nombre = txtMedicoNombre.getText();
            String especialidad = txtMedicoEspecialidad.getText();
            String cedula = txtMedicoCedula.getText();
            String correo = txtMedicoCorreo.getText();

            Medico medico = new Medico(id, nombre, especialidad, cedula, correo);
            boolean actualizado = medicoService.actualizarMedico(medico);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Médico actualizado exitosamente.");
                limpiarCamposMedico();
                cargarTodosMedicos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el médico.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de médico inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMedico() {
        String idText = txtMedicoId.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico de la tabla o ingrese un ID para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este médico?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idText);
                boolean eliminado = medicoService.eliminarMedico(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Médico eliminado exitosamente.");
                    limpiarCamposMedico();
                    cargarTodosMedicos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el médico.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de médico inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar médico: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarMedicosPorEspecialidad() {
        String especialidad = txtMedicoEspecialidad.getText().trim();
        if (especialidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una especialidad para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelMedicos.setRowCount(0); // Limpiar tabla
        try {
            List<Medico> medicos = medicoService.buscarMedicosPorEspecialidad(especialidad);
            if (!medicos.isEmpty()) {
                for (Medico m : medicos) {
                    modelMedicos.addRow(new Object[]{m.getId(), m.getNombre(), m.getEspecialidad(), m.getCedula(), m.getCorreoElectronico()});
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron médicos con la especialidad: " + especialidad, "No Encontrado", JOptionPane.INFORMATION_MESSAGE);
                cargarTodosMedicos(); // Recargar todos para mostrar la lista original
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar médicos por especialidad: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTodosMedicos() {
        modelMedicos.setRowCount(0); // Limpiar tabla
        try {
            List<Medico> medicos = medicoService.obtenerTodosMedicos();
            for (Medico m : medicos) {
                modelMedicos.addRow(new Object[]{m.getId(), m.getNombre(), m.getEspecialidad(), m.getCedula(), m.getCorreoElectronico()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarMedicoSeleccionado() {
        int selectedRow = tblMedicos.getSelectedRow();
        if (selectedRow >= 0) {
            txtMedicoId.setText(modelMedicos.getValueAt(selectedRow, 0).toString());
            txtMedicoNombre.setText(modelMedicos.getValueAt(selectedRow, 1).toString());
            txtMedicoEspecialidad.setText(modelMedicos.getValueAt(selectedRow, 2).toString());
            txtMedicoCedula.setText(modelMedicos.getValueAt(selectedRow, 3).toString());
            txtMedicoCorreo.setText(modelMedicos.getValueAt(selectedRow, 4).toString());
        }
    }

    private void limpiarCamposMedico() {
        txtMedicoId.setText("");
        txtMedicoNombre.setText("");
        txtMedicoEspecialidad.setText("");
        txtMedicoCedula.setText("");
        txtMedicoCorreo.setText("");
        cargarTodosMedicos();
    }

    private boolean validarCamposMedico(boolean isUpdate) {
        if (isUpdate && txtMedicoId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico o ingrese un ID para actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtMedicoNombre.getText().trim().isEmpty() ||
                txtMedicoEspecialidad.getText().trim().isEmpty() ||
                txtMedicoCedula.getText().trim().isEmpty() ||
                txtMedicoCorreo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        // Validación básica de correo electrónico
        if (!txtMedicoCorreo.getText().trim().matches(EMAIL_REGEX)) {
            JOptionPane.showMessageDialog(this, "Formato de Correo Electrónico incorrecto.", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // --- Métodos para Citas ---

    private void crearCita() {
        if (!validarCamposCita(false)) {
            return;
        }

        SimpleDateFormat sdfInput = new SimpleDateFormat("dd-MM-yyyy");
        Date fecha = null;
        try {
            fecha = sdfInput.parse(txtCitaFecha.getText().trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use DD-MM-YYYY.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hora = txtCitaHora.getText().trim();
        String motivo = txtCitaMotivo.getText().trim();
        int medicoId, pacienteId;
        try {
            medicoId = Integer.parseInt(txtCitaMedicoId.getText().trim());
            pacienteId = Integer.parseInt(txtCitaPacienteId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de médico o paciente inválido. Deben ser números enteros.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cita cita = new Cita(fecha, hora, motivo, medicoId, pacienteId);
        try {
            int id = citaService.crearCita(cita);
            JOptionPane.showMessageDialog(this, "Cita creada con ID: " + id);
            limpiarCamposCita();
            cargarTodasLasCitas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear cita: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCita() {
        if (!validarCamposCita(true)) {
            return;
        }

        int id;
        try {
            id = Integer.parseInt(txtCitaId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de cita inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat sdfInput = new SimpleDateFormat("dd-MM-yyyy");
        Date fecha = null;
        try {
            fecha = sdfInput.parse(txtCitaFecha.getText().trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use DD-MM-YYYY.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String hora = txtCitaHora.getText().trim();
        String motivo = txtCitaMotivo.getText().trim();
        int medicoId, pacienteId;
        try {
            medicoId = Integer.parseInt(txtCitaMedicoId.getText().trim());
            pacienteId = Integer.parseInt(txtCitaPacienteId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de médico o paciente inválido. Deben ser números enteros.", "Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Cita cita = new Cita(id, fecha, hora, motivo, medicoId, pacienteId);
        try {
            boolean actualizado = citaService.actualizarCita(cita);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Cita actualizada exitosamente.");
                limpiarCamposCita();
                cargarTodasLasCitas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar cita: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCita() {
        String idText = txtCitaId.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita de la tabla o ingrese un ID para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta cita?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idText);
                boolean eliminado = citaService.eliminarCita(id);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Cita eliminada exitosamente.");
                    limpiarCamposCita();
                    cargarTodasLasCitas();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de cita inválido.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar cita: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para buscar médicos por especialidad, invocado desde el panel de citas
    private void buscarMedicosPorEspecialidadParaCita() {
        String especialidadBusqueda = JOptionPane.showInputDialog(this, "Ingrese la especialidad para buscar médicos:");
        if (especialidadBusqueda == null || especialidadBusqueda.trim().isEmpty()) {
            return; // El usuario canceló o ingresó una cadena vacía
        }

        // Limpiar la tabla de médicos para mostrar los resultados de la búsqueda
        modelMedicos.setRowCount(0);
        try {
            // Reutilizamos el método existente en CitaInterfaz, que tiene un método `buscarMedicosPorFecha(String especialidad)`
            // aunque su nombre original `buscarMedicosPorFecha` puede ser confuso.
            List<Medico> medicos = citaService.buscarMedicosPorFecha(especialidadBusqueda);
            if (!medicos.isEmpty()) {
                tabbedPane.setSelectedComponent(tabbedPane.getComponentAt(1)); // Cambiar a la pestaña de Médicos
                for (Medico m : medicos) {
                    modelMedicos.addRow(new Object[]{m.getId(), m.getNombre(), m.getEspecialidad(), m.getCedula(), m.getCorreoElectronico()});
                }
                JOptionPane.showMessageDialog(this, "Médicos encontrados para la especialidad '" + especialidadBusqueda + "'. Mostrando en la pestaña Médicos.", "Resultados de Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron médicos para la especialidad: " + especialidadBusqueda, "No Encontrado", JOptionPane.INFORMATION_MESSAGE);
                cargarTodosMedicos(); // Recargar todos para mostrar la lista original en la pestaña de Médicos
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al buscar médicos por especialidad: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            cargarTodosMedicos(); // Asegurarse de que la tabla de médicos se recargue en caso de error
        }
    }


    private void cargarTodasLasCitas() {
        modelCitas.setRowCount(0); // Limpiar tabla
        try {
            List<Cita> citas = citaService.obtenerTodasLasCitas();
            SimpleDateFormat sdfTabla = new SimpleDateFormat("dd-MM-yyyy"); // Formato para mostrar en la tabla
            for (Cita c : citas) {
                modelCitas.addRow(new Object[]{
                        c.getId(),
                        sdfTabla.format(c.getFecha()),
                        c.getHora(), // La hora ya viene en formato de 24 horas si así se ingresó
                        c.getMotivo(),
                        c.getMedicoId(),
                        c.getPacienteId(),
                        c.getMedicoNombre() != null ? c.getMedicoNombre() : "N/A", // Mostrar "N/A" si es nulo
                        c.getPacienteNombre() != null ? c.getPacienteNombre() : "N/A" // Mostrar "N/A" si es nulo
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar citas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarCitaSeleccionada() {
        int selectedRow = tblCitas.getSelectedRow();
        if (selectedRow >= 0) {
            txtCitaId.setText(modelCitas.getValueAt(selectedRow, 0).toString());
            txtCitaFecha.setText(modelCitas.getValueAt(selectedRow, 1).toString()); // Cargar fecha
            txtCitaHora.setText(modelCitas.getValueAt(selectedRow, 2).toString()); // Cargar hora
            txtCitaMotivo.setText(modelCitas.getValueAt(selectedRow, 3).toString());
            txtCitaMedicoId.setText(modelCitas.getValueAt(selectedRow, 4).toString());
            txtCitaPacienteId.setText(modelCitas.getValueAt(selectedRow, 5).toString());
        }
    }

    private void limpiarCamposCita() {
        txtCitaId.setText("");
        txtCitaFecha.setText(""); // Limpiar campo de fecha
        txtCitaHora.setText(""); // Limpiar campo de hora
        txtCitaMotivo.setText("");
        txtCitaMedicoId.setText("");
        txtCitaPacienteId.setText("");
        cargarTodasLasCitas();
    }

    private boolean validarCamposCita(boolean isUpdate) {
        if (isUpdate && txtCitaId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita o ingrese un ID para actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtCitaFecha.getText().trim().isEmpty() ||
                txtCitaHora.getText().trim().isEmpty() ||
                txtCitaMotivo.getText().trim().isEmpty() ||
                txtCitaMedicoId.getText().trim().isEmpty() ||
                txtCitaPacienteId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validar formato de Fecha (DD-MM-YYYY)
        SimpleDateFormat sdfValidate = new SimpleDateFormat("dd-MM-yyyy");
        sdfValidate.setLenient(false); // Estricto para el formato
        try {
            sdfValidate.parse(txtCitaFecha.getText().trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use DD-MM-YYYY (ej. 31-12-2025).", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar formato de Hora (HH:MM) - Usa la expresión regular para 24 horas
        if (!txtCitaHora.getText().trim().matches(HORA_REGEX)) {
            JOptionPane.showMessageDialog(this, "Formato de hora incorrecto. Use HH:MM (ej. 14:30).", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar que MedicoId y PacienteId sean números enteros
        try {
            Integer.parseInt(txtCitaMedicoId.getText().trim());
            Integer.parseInt(txtCitaPacienteId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los campos ID Médico y ID Paciente deben ser números enteros válidos.", "Validación", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Cliente::new);
    }
}