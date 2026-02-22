package com.rentacar.rest;

import com.rentacar.dtos.ClienteDTO;
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
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private AlquilerService alquilerService;

    @Operation(summary = "Listar todos los clientes")
    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return alquilerService.listarClientesDTO();
    }

    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto (email duplicado)")
    })
    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevoCliente = alquilerService.guardarClienteDTO(clienteDTO);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener cliente por DNI")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{dni}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable String dni) {
        return alquilerService.obtenerClienteDTOPorDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar cliente por DNI")
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable String dni) {
        if (alquilerService.obtenerClienteDTOPorDni(dni).isPresent()) {
            alquilerService.eliminarCliente(dni);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
