package com.rentacar.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "vehiculos")
public class Vehiculo {

    @Id
    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El precio por día es obligatorio")
    @Min(value = 0, message = "El precio debe ser mayor o igual a 0")
    private Double precioDia;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El estado es obligatorio")
    private EstadoVehiculo estado;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    private List<Alquiler> alquileres;

    public enum EstadoVehiculo {
        DISPONIBLE, ALQUILADO, EN_REPARACION
    }

    public Vehiculo() {
    }

    public Vehiculo(String matricula, String modelo, Double precioDia, EstadoVehiculo estado) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.precioDia = precioDia;
        this.estado = estado;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Double getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(Double precioDia) {
        this.precioDia = precioDia;
    }

    public EstadoVehiculo getEstado() {
        return estado;
    }

    public void setEstado(EstadoVehiculo estado) {
        this.estado = estado;
    }

    public List<Alquiler> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<Alquiler> alquileres) {
        this.alquileres = alquileres;
    }
}
