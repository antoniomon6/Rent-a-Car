package com.rentacar.services;

import com.rentacar.entidades.Alquiler;
import com.rentacar.entidades.Cliente;
import com.rentacar.entidades.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface AlquilerService {
    Alquiler crearAlquiler(Alquiler alquiler);
    List<Alquiler> listarAlquileres();
    Optional<Alquiler> obtenerAlquilerPorId(Long id);
    void eliminarAlquiler(Long id);

    // Métodos para Cliente y Vehiculo
    Cliente guardarCliente(Cliente cliente);
    List<Cliente> listarClientes();
    Optional<Cliente> obtenerClientePorDni(String dni);
    void eliminarCliente(String dni);

    Vehiculo guardarVehiculo(Vehiculo vehiculo);
    List<Vehiculo> listarVehiculos();
    Optional<Vehiculo> obtenerVehiculoPorMatricula(String matricula);
    void eliminarVehiculo(String matricula);
    List<Vehiculo> listarVehiculosPorPrecio(Double min, Double max);
}
