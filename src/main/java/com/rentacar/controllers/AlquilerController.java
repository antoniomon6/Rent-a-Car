package com.rentacar.controllers;

import com.rentacar.entidades.Alquiler;
import com.rentacar.excepciones.ReglaNegocioException;
import com.rentacar.services.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping
    public String listarAlquileres(Model model) {
        model.addAttribute("alquileres", alquilerService.listarAlquileres());
        return "alquileres";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("alquiler", new Alquiler());
        model.addAttribute("clientes", alquilerService.listarClientes());
        model.addAttribute("vehiculos", alquilerService.listarVehiculos());
        return "form-alquiler";
    }

    @PostMapping
    public String guardarAlquiler(@Valid @ModelAttribute("alquiler") Alquiler alquiler, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", alquilerService.listarClientes());
            model.addAttribute("vehiculos", alquilerService.listarVehiculos());
            return "form-alquiler";
        }
        try {
            alquilerService.crearAlquiler(alquiler);
        } catch (ReglaNegocioException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", alquilerService.listarClientes());
            model.addAttribute("vehiculos", alquilerService.listarVehiculos());
            return "form-alquiler";
        }
        return "redirect:/alquileres";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAlquiler(@PathVariable Long id) {
        alquilerService.eliminarAlquiler(id);
        return "redirect:/alquileres";
    }
}
