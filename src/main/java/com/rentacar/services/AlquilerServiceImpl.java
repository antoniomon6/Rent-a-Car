package com.rentacar.services;

import com.rentacar.dtos.AlquilerDTO;
import com.rentacar.dtos.ClienteDTO;
import com.rentacar.dtos.VehiculoDTO;
import com.rentacar.entidades.Alquiler;
import com.rentacar.entidades.Cliente;
import com.rentacar.entidades.Vehiculo;
import com.rentacar.excepciones.ReglaNegocioException;
import com.rentacar.repositories.AlquilerRepository;
import com.rentacar.repositories.ClienteRepository;
import com.rentacar.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    // --- Métodos para Alquiler ---

    @Override
    @Transactional
    public Alquiler crearAlquiler(Alquiler alquiler) {
        //  Validación de fechas
        if (alquiler.getFechaFin().isBefore(alquiler.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        // Disponibilidad
        Vehiculo vehiculo = vehiculoRepository.findById(alquiler.getVehiculo().getMatricula())
                .orElseThrow(() -> new ReglaNegocioException("Vehículo no encontrado"));

        if (vehiculo.getEstado() == Vehiculo.EstadoVehiculo.EN_REPARACION) {
            throw new ReglaNegocioException("El vehículo está en reparación");
        }

        List<Alquiler> conflictos = alquilerRepository.findConflictingRentals(
                vehiculo.getMatricula(), alquiler.getFechaInicio(), alquiler.getFechaFin());

        if (!conflictos.isEmpty()) {
            throw new ReglaNegocioException("El vehículo ya está alquilado en esas fechas");
        }

        // Cálculo automático
        long dias = ChronoUnit.DAYS.between(alquiler.getFechaInicio(), alquiler.getFechaFin());
        alquiler.setCostoTotal(dias * vehiculo.getPrecioDia());

        // Actualizar estado del vehículo a ALQUILADO
        vehiculo.setEstado(Vehiculo.EstadoVehiculo.ALQUILADO);
        vehiculoRepository.save(vehiculo);

        return alquilerRepository.save(alquiler);
    }

    @Override
    @Transactional
    public AlquilerDTO crearAlquilerDTO(AlquilerDTO alquilerDTO) {
        Cliente cliente = clienteRepository.findById(alquilerDTO.getClienteDni())
                .orElseThrow(() -> new ReglaNegocioException("Cliente no encontrado"));
        Vehiculo vehiculo = vehiculoRepository.findById(alquilerDTO.getVehiculoMatricula())
                .orElseThrow(() -> new ReglaNegocioException("Vehículo no encontrado"));

        Alquiler alquiler = new Alquiler();
        alquiler.setFechaInicio(alquilerDTO.getFechaInicio());
        alquiler.setFechaFin(alquilerDTO.getFechaFin());
        alquiler.setCliente(cliente);
        alquiler.setVehiculo(vehiculo);

        Alquiler nuevoAlquiler = crearAlquiler(alquiler);
        return mapToAlquilerDTO(nuevoAlquiler);
    }

    @Override
    @Transactional
    public void devolverVehiculo(Long alquilerId) {
        Alquiler alquiler = alquilerRepository.findById(alquilerId)
                .orElseThrow(() -> new ReglaNegocioException("Alquiler no encontrado"));
        
        Vehiculo vehiculo = alquiler.getVehiculo();
        vehiculo.setEstado(Vehiculo.EstadoVehiculo.DISPONIBLE);
        vehiculoRepository.save(vehiculo);
    }

    @Override
    public List<Alquiler> listarAlquileres() {
        return alquilerRepository.findAll();
    }

    @Override
    public List<AlquilerDTO> listarAlquileresDTO() {
        return alquilerRepository.findAll().stream()
                .map(this::mapToAlquilerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Alquiler> obtenerAlquilerPorId(Long id) {
        return alquilerRepository.findById(id);
    }

    @Override
    public Optional<AlquilerDTO> obtenerAlquilerDTOPorId(Long id) {
        return alquilerRepository.findById(id).map(this::mapToAlquilerDTO);
    }

    @Override
    public void eliminarAlquiler(Long id) {
        alquilerRepository.deleteById(id);
    }

    @Override
    public Double obtenerIngresosPorMes(int mes, int anio) {
        return alquilerRepository.calculateIncomeByMonthAndYear(mes, anio);
    }

    @Override
    public List<Alquiler> obtenerHistorialCliente(String dni) {
        return alquilerRepository.findByClienteDni(dni);
    }

    // --- Métodos para Cliente ---

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ReglaNegocioException("El email ya está registrado");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public ClienteDTO guardarClienteDTO(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setDni(clienteDTO.getDni());
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefono(clienteDTO.getTelefono());
        
        Cliente nuevoCliente = guardarCliente(cliente);
        return mapToClienteDTO(nuevoCliente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public List<ClienteDTO> listarClientesDTO() {
        return clienteRepository.findAll().stream()
                .map(this::mapToClienteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> listarClientesPorFacturacion() {
        return alquilerRepository.findTopClientesByFacturacion();
    }

    @Override
    public Optional<Cliente> obtenerClientePorDni(String dni) {
        return clienteRepository.findById(dni);
    }

    @Override
    public Optional<ClienteDTO> obtenerClienteDTOPorDni(String dni) {
        return clienteRepository.findById(dni).map(this::mapToClienteDTO);
    }

    @Override
    public void eliminarCliente(String dni) {
        clienteRepository.deleteById(dni);
    }

    // --- Métodos para Vehiculo ---

    @Override
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public VehiculoDTO guardarVehiculoDTO(VehiculoDTO vehiculoDTO) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMatricula(vehiculoDTO.getMatricula());
        vehiculo.setModelo(vehiculoDTO.getModelo());
        vehiculo.setPrecioDia(vehiculoDTO.getPrecioDia());
        vehiculo.setEstado(vehiculoDTO.getEstado());

        Vehiculo nuevoVehiculo = guardarVehiculo(vehiculo);
        return mapToVehiculoDTO(nuevoVehiculo);
    }

    @Override
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Override
    public List<VehiculoDTO> listarVehiculosDTO() {
        return vehiculoRepository.findAll().stream()
                .map(this::mapToVehiculoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Vehiculo> obtenerVehiculoPorMatricula(String matricula) {
        return vehiculoRepository.findById(matricula);
    }

    @Override
    public Optional<VehiculoDTO> obtenerVehiculoDTOPorMatricula(String matricula) {
        return vehiculoRepository.findById(matricula).map(this::mapToVehiculoDTO);
    }

    @Override
    public void eliminarVehiculo(String matricula) {
        vehiculoRepository.deleteById(matricula);
    }

    @Override
    public List<Vehiculo> listarVehiculosPorPrecio(Double min, Double max) {
        return vehiculoRepository.findByPrecioDiaBetween(min, max);
    }

    // --- Mappers ---

    private AlquilerDTO mapToAlquilerDTO(Alquiler alquiler) {
        return new AlquilerDTO(
                alquiler.getId(),
                alquiler.getFechaInicio(),
                alquiler.getFechaFin(),
                alquiler.getCostoTotal(),
                alquiler.getCliente().getDni(),
                alquiler.getVehiculo().getMatricula()
        );
    }

    private ClienteDTO mapToClienteDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getEmail(),
                cliente.getTelefono()
        );
    }

    private VehiculoDTO mapToVehiculoDTO(Vehiculo vehiculo) {
        return new VehiculoDTO(
                vehiculo.getMatricula(),
                vehiculo.getModelo(),
                vehiculo.getPrecioDia(),
                vehiculo.getEstado()
        );
    }
}
