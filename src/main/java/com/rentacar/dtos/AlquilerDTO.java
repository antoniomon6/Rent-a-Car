package com.rentacar.dtos;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AlquilerDTO {
    private Long id;
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;
    private Double costoTotal;
    @NotNull(message = "El DNI del cliente es obligatorio")
    private String clienteDni;
    @NotNull(message = "La matrícula del vehículo es obligatoria")
    private String vehiculoMatricula;

    // Constructores, Getters y Setters
    public AlquilerDTO() {}
    public AlquilerDTO(Long id, LocalDate fechaInicio, LocalDate fechaFin, Double costoTotal, String clienteDni, String vehiculoMatricula) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoTotal = costoTotal;
        this.clienteDni = clienteDni;
        this.vehiculoMatricula = vehiculoMatricula;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(Double costoTotal) { this.costoTotal = costoTotal; }
    public String getClienteDni() { return clienteDni; }
    public void setClienteDni(String clienteDni) { this.clienteDni = clienteDni; }
    public String getVehiculoMatricula() { return vehiculoMatricula; }
    public void setVehiculoMatricula(String vehiculoMatricula) { this.vehiculoMatricula = vehiculoMatricula; }
}
