package com.rentacar.config;

import com.rentacar.entidades.Alquiler;
import com.rentacar.entidades.Cliente;
import com.rentacar.entidades.Vehiculo;
import com.rentacar.repositories.AlquilerRepository;
import com.rentacar.repositories.ClienteRepository;
import com.rentacar.repositories.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear Clientes
        Cliente juan = createClienteIfNotFound("12345678A", "Juan Pérez", "juan.perez@example.com", "600123456");
        Cliente ana = createClienteIfNotFound("87654321B", "Ana García", "ana.garcia@example.com", "600654321");
        Cliente carlos = createClienteIfNotFound("11223344C", "Carlos López", "carlos.lopez@example.com", "600112233");

        // 2. Crear Vehículos
        Vehiculo toyota = createVehiculoIfNotFound("1234ABC", "Toyota Corolla", 50.0, Vehiculo.EstadoVehiculo.DISPONIBLE);
        Vehiculo ford = createVehiculoIfNotFound("5678DEF", "Ford Focus", 45.0, Vehiculo.EstadoVehiculo.DISPONIBLE);
        Vehiculo bmw = createVehiculoIfNotFound("9012GHI", "BMW Serie 3", 80.0, Vehiculo.EstadoVehiculo.EN_REPARACION);
        Vehiculo audi = createVehiculoIfNotFound("3456JKL", "Audi A4", 85.0, Vehiculo.EstadoVehiculo.DISPONIBLE);

        // 3. Crear Alquileres de ejemplo (solo si la tabla está vacía)
        if (alquilerRepository.count() == 0) {
            
            // Caso A: Alquiler activo (Juan tiene Toyota)
            createAlquiler(juan, toyota, LocalDate.now(), LocalDate.now().plusDays(5));
            // Actualizar estado del vehículo
            toyota.setEstado(Vehiculo.EstadoVehiculo.ALQUILADO);
            vehiculoRepository.save(toyota);

            // Caso B: Alquiler futuro (Ana reservó Ford)
            createAlquiler(ana, ford, LocalDate.now().plusDays(10), LocalDate.now().plusDays(15));
            
            // Caso C: Alquiler pasado (Carlos tuvo Audi)
            createAlquiler(carlos, audi, LocalDate.now().minusDays(10), LocalDate.now().minusDays(5));
        }
    }

    private Cliente createClienteIfNotFound(String dni, String nombre, String email, String telefono) {
        return clienteRepository.findById(dni).orElseGet(() -> {
            Cliente cliente = new Cliente(dni, nombre, email, telefono);
            return clienteRepository.save(cliente);
        });
    }

    private Vehiculo createVehiculoIfNotFound(String matricula, String modelo, Double precioDia, Vehiculo.EstadoVehiculo estado) {
        return vehiculoRepository.findById(matricula).orElseGet(() -> {
            Vehiculo vehiculo = new Vehiculo(matricula, modelo, precioDia, estado);
            return vehiculoRepository.save(vehiculo);
        });
    }

    private void createAlquiler(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFin) {
        Alquiler alquiler = new Alquiler(fechaInicio, fechaFin, cliente, vehiculo);
        // Calcular costo total simple para el ejemplo
        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        alquiler.setCostoTotal(dias * vehiculo.getPrecioDia());
        alquilerRepository.save(alquiler);
    }
}
