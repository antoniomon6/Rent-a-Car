package com.rentacar.controllers;

import com.rentacar.entidades.Vehiculo;
import com.rentacar.services.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", alquilerService.listarVehiculos());
        return "vehiculos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "form-vehiculo";
    }

    @PostMapping
    public String guardarVehiculo(@Valid @ModelAttribute("vehiculo") Vehiculo vehiculo, BindingResult result) {
        if (result.hasErrors()) {
            return "form-vehiculo";
        }
        alquilerService.guardarVehiculo(vehiculo);
        return "redirect:/vehiculos";
    }

    @GetMapping("/eliminar/{matricula}")
    public String eliminarVehiculo(@PathVariable String matricula) {
        alquilerService.eliminarVehiculo(matricula);
        return "redirect:/vehiculos";
    }
}
