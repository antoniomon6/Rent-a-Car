package com.rentacar.rest;

import com.rentacar.entidades.Vehiculo;
import com.rentacar.services.AlquilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoRestController {

    @Autowired
    private AlquilerService alquilerService;

    @Operation(summary = "Listar todos los vehículos")
    @GetMapping
    public List<Vehiculo> listarVehiculos() {
        return alquilerService.listarVehiculos();
    }

    @Operation(summary = "Crear un nuevo vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Vehiculo> crearVehiculo(@Valid @RequestBody Vehiculo vehiculo) {
        Vehiculo nuevoVehiculo = alquilerService.guardarVehiculo(vehiculo);
        return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener vehículo por Matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{matricula}")
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable String matricula) {
        return alquilerService.obtenerVehiculoPorMatricula(matricula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar vehículo por Matrícula")
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable String matricula) {
        if (alquilerService.obtenerVehiculoPorMatricula(matricula).isPresent()) {
            alquilerService.eliminarVehiculo(matricula);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
