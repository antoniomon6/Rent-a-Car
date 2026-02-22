package com.rentacar.dtos;

import com.rentacar.entidades.Vehiculo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehiculoDTO {
    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    @NotNull(message = "El precio por día es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Double precioDia;
    @NotNull(message = "El estado es obligatorio")
    private Vehiculo.EstadoVehiculo estado;

    public VehiculoDTO() {}
    public VehiculoDTO(String matricula, String modelo, Double precioDia, Vehiculo.EstadoVehiculo estado) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.precioDia = precioDia;
        this.estado = estado;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public Double getPrecioDia() { return precioDia; }
    public void setPrecioDia(Double precioDia) { this.precioDia = precioDia; }
    public Vehiculo.EstadoVehiculo getEstado() { return estado; }
    public void setEstado(Vehiculo.EstadoVehiculo estado) { this.estado = estado; }
}
