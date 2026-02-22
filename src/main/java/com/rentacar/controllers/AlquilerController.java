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
import java.time.LocalDate;

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

    // UC 5: Devolver Vehículo
    @GetMapping("/devolver/{id}")
    public String devolverVehiculo(@PathVariable Long id) {
        alquilerService.devolverVehiculo(id);
        return "redirect:/alquileres";
    }

    // UC 7: Reporte de Ingresos
    @GetMapping("/reportes/ingresos")
    public String reporteIngresos(@RequestParam(required = false) Integer mes, 
                                  @RequestParam(required = false) Integer anio, 
                                  Model model) {
        if (mes == null) mes = LocalDate.now().getMonthValue();
        if (anio == null) anio = LocalDate.now().getYear();
        
        Double ingresos = alquilerService.obtenerIngresosPorMes(mes, anio);
        model.addAttribute("ingresos", ingresos);
        model.addAttribute("mes", mes);
        model.addAttribute("anio", anio);
        return "reporte-ingresos";
    }

    // UC 8: Historial Cliente
    @GetMapping("/historial/{dni}")
    public String historialCliente(@PathVariable String dni, Model model) {
        model.addAttribute("alquileres", alquilerService.obtenerHistorialCliente(dni));
        model.addAttribute("cliente", alquilerService.obtenerClientePorDni(dni).orElse(null));
        return "historial-cliente";
    }
}
