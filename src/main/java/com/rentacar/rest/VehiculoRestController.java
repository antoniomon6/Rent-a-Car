package com.rentacar.rest;

import com.rentacar.dtos.VehiculoDTO;
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
    public List<VehiculoDTO> listarVehiculos() {
        return alquilerService.listarVehiculosDTO();
    }

    @Operation(summary = "Crear un nuevo vehículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehículo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<VehiculoDTO> crearVehiculo(@Valid @RequestBody VehiculoDTO vehiculoDTO) {
        VehiculoDTO nuevoVehiculo = alquilerService.guardarVehiculoDTO(vehiculoDTO);
        return new ResponseEntity<>(nuevoVehiculo, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener vehículo por Matrícula")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/{matricula}")
    public ResponseEntity<VehiculoDTO> obtenerVehiculo(@PathVariable String matricula) {
        return alquilerService.obtenerVehiculoDTOPorMatricula(matricula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar vehículo por Matrícula")
    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable String matricula) {
        if (alquilerService.obtenerVehiculoDTOPorMatricula(matricula).isPresent()) {
            alquilerService.eliminarVehiculo(matricula);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
