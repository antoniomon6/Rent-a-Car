package com.rentacar.services;

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

@Service
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    @Transactional
    public Alquiler crearAlquiler(Alquiler alquiler) {
        // Validación de fechas
        if (alquiler.getFechaFin().isBefore(alquiler.getFechaInicio())) {
            throw new ReglaNegocioException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        // Validación de disponibilidad del vehículo
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

        // Cálculo automático del costo total
        long dias = ChronoUnit.DAYS.between(alquiler.getFechaInicio(), alquiler.getFechaFin());
        alquiler.setCostoTotal(dias * vehiculo.getPrecioDia());

        // Actualizar estado del vehículo a ALQUILADO
        vehiculo.setEstado(Vehiculo.EstadoVehiculo.ALQUILADO);
        vehiculoRepository.save(vehiculo);

        return alquilerRepository.save(alquiler);
    }

    @Override
    public List<Alquiler> listarAlquileres() {
        return alquilerRepository.findAll();
    }

    @Override
    public Optional<Alquiler> obtenerAlquilerPorId(Long id) {
        return alquilerRepository.findById(id);
    }

    @Override
    public void eliminarAlquiler(Long id) {
        alquilerRepository.deleteById(id);
    }

    // Métodos para Cliente
    @Override
    public Cliente guardarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ReglaNegocioException("El email ya está registrado");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> obtenerClientePorDni(String dni) {
        return clienteRepository.findById(dni);
    }

    @Override
    public void eliminarCliente(String dni) {
        clienteRepository.deleteById(dni);
    }

    // Métodos para Vehiculo
    @Override
    public Vehiculo guardarVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.findAll();
    }

    @Override
    public Optional<Vehiculo> obtenerVehiculoPorMatricula(String matricula) {
        return vehiculoRepository.findById(matricula);
    }

    @Override
    public void eliminarVehiculo(String matricula) {
        vehiculoRepository.deleteById(matricula);
    }

    @Override
    public List<Vehiculo> listarVehiculosPorPrecio(Double min, Double max) {
        return vehiculoRepository.findByPrecioDiaBetween(min, max);
    }
}
