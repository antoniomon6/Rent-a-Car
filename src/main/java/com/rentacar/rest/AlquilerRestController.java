package com.rentacar.rest;

import com.rentacar.entidades.Alquiler;
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
    public List<Alquiler> listarAlquileres() {
        return alquilerService.listarAlquileres();
    }

    @Operation(summary = "Crear un nuevo alquiler")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alquiler creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto de negocio (fechas, disponibilidad)")
    })
    @PostMapping
    public ResponseEntity<Alquiler> crearAlquiler(@Valid @RequestBody Alquiler alquiler) {
        Alquiler nuevoAlquiler = alquilerService.crearAlquiler(alquiler);
        return new ResponseEntity<>(nuevoAlquiler, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener alquiler por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alquiler encontrado"),
            @ApiResponse(responseCode = "404", description = "Alquiler no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Alquiler> obtenerAlquiler(@PathVariable Long id) {
        return alquilerService.obtenerAlquilerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar alquiler por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlquiler(@PathVariable Long id) {
        if (alquilerService.obtenerAlquilerPorId(id).isPresent()) {
            alquilerService.eliminarAlquiler(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
