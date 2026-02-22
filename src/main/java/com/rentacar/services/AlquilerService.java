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
    // --- Alquileres ---
    Alquiler crearAlquiler(Alquiler alquiler);
    AlquilerDTO crearAlquilerDTO(AlquilerDTO alquilerDTO);
    List<Alquiler> listarAlquileres();
    List<AlquilerDTO> listarAlquileresDTO();
    Optional<Alquiler> obtenerAlquilerPorId(Long id);
    Optional<AlquilerDTO> obtenerAlquilerDTOPorId(Long id);
    void eliminarAlquiler(Long id);
    
    // Devolver Vehículo
    void devolverVehiculo(Long alquilerId);

    // Ingresos por mes
    Double obtenerIngresosPorMes(int mes, int anio);

    // Historial Cliente
    List<Alquiler> obtenerHistorialCliente(String dni);

    // --- Clientes ---
    Cliente guardarCliente(Cliente cliente);
    ClienteDTO guardarClienteDTO(ClienteDTO clienteDTO);
    List<Cliente> listarClientes();
    List<ClienteDTO> listarClientesDTO();
    Optional<Cliente> obtenerClientePorDni(String dni);
    Optional<ClienteDTO> obtenerClienteDTOPorDni(String dni);
    void eliminarCliente(String dni);
    
    // Clientes por facturación
    List<Cliente> listarClientesPorFacturacion();

    // --- Vehículos ---
    Vehiculo guardarVehiculo(Vehiculo vehiculo);
    VehiculoDTO guardarVehiculoDTO(VehiculoDTO vehiculoDTO);
    List<Vehiculo> listarVehiculos();
    List<VehiculoDTO> listarVehiculosDTO();
    Optional<Vehiculo> obtenerVehiculoPorMatricula(String matricula);
    Optional<VehiculoDTO> obtenerVehiculoDTOPorMatricula(String matricula);
    void eliminarVehiculo(String matricula);
    
    // Vehículos por precio
    List<Vehiculo> listarVehiculosPorPrecio(Double min, Double max);
}
