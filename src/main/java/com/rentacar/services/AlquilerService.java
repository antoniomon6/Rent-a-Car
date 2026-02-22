package com.rentacar.services;

import com.rentacar.dtos.AlquilerDTO;
import com.rentacar.dtos.ClienteDTO;
import com.rentacar.dtos.VehiculoDTO;
import com.rentacar.entidades.Alquiler;
import com.rentacar.entidades.Cliente;
import com.rentacar.entidades.Vehiculo;
import java.util.List;
import java.util.Optional;

public interface AlquilerService {
    // Métodos para Alquiler
    Alquiler crearAlquiler(Alquiler alquiler); // Mantenemos para MVC
    AlquilerDTO crearAlquilerDTO(AlquilerDTO alquilerDTO); // Nuevo para REST
    List<Alquiler> listarAlquileres();
    List<AlquilerDTO> listarAlquileresDTO(); // Nuevo para REST
    Optional<Alquiler> obtenerAlquilerPorId(Long id);
    Optional<AlquilerDTO> obtenerAlquilerDTOPorId(Long id); // Nuevo para REST
    void eliminarAlquiler(Long id);

    // Métodos para Cliente
    Cliente guardarCliente(Cliente cliente); // Mantenemos para MVC
    ClienteDTO guardarClienteDTO(ClienteDTO clienteDTO); // Nuevo para REST
    List<Cliente> listarClientes();
    List<ClienteDTO> listarClientesDTO(); // Nuevo para REST
    Optional<Cliente> obtenerClientePorDni(String dni);
    Optional<ClienteDTO> obtenerClienteDTOPorDni(String dni); // Nuevo para REST
    void eliminarCliente(String dni);

    // Métodos para Vehiculo
    Vehiculo guardarVehiculo(Vehiculo vehiculo); // Mantenemos para MVC
    VehiculoDTO guardarVehiculoDTO(VehiculoDTO vehiculoDTO); // Nuevo para REST
    List<Vehiculo> listarVehiculos();
    List<VehiculoDTO> listarVehiculosDTO(); // Nuevo para REST
    Optional<Vehiculo> obtenerVehiculoPorMatricula(String matricula);
    Optional<VehiculoDTO> obtenerVehiculoDTOPorMatricula(String matricula); // Nuevo para REST
    void eliminarVehiculo(String matricula);
    List<Vehiculo> listarVehiculosPorPrecio(Double min, Double max);
}
