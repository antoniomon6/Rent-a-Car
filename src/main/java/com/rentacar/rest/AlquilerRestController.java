package com.rentacar.rest;

import com.rentacar.dtos.AlquilerDTO;
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
@RequestMapping("/api/alquileres")
public class AlquilerRestController {

    @Autowired
    private AlquilerService alquilerService;

    @Operation(summary = "Listar todos los alquileres")
    @GetMapping
    public List<AlquilerDTO> listarAlquileres() {
        return alquilerService.listarAlquileresDTO();
    }

    @Operation(summary = "Crear un nuevo alquiler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alquiler creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio (fechas, disponibilidad)")
    })
    @PostMapping
    public ResponseEntity<AlquilerDTO> crearAlquiler(@Valid @RequestBody AlquilerDTO alquilerDTO) {
        AlquilerDTO nuevoAlquiler = alquilerService.crearAlquilerDTO(alquilerDTO);
        return new ResponseEntity<>(nuevoAlquiler, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener alquiler por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alquiler encontrado"),
            @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlquilerDTO> obtenerAlquiler(@PathVariable Long id) {
        return alquilerService.obtenerAlquilerDTOPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar alquiler por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlquiler(@PathVariable Long id) {
        if (alquilerService.obtenerAlquilerDTOPorId(id).isPresent()) {
            alquilerService.eliminarAlquiler(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
