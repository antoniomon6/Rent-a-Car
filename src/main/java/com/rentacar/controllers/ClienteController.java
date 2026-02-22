package com.rentacar.controllers;

import com.rentacar.entidades.Cliente;
import com.rentacar.services.AlquilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", alquilerService.listarClientes());
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "form-cliente";
    }

    @PostMapping
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result) {
        if (result.hasErrors()) {
            return "form-cliente";
        }
        alquilerService.guardarCliente(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{dni}")
    public String eliminarCliente(@PathVariable String dni) {
        alquilerService.eliminarCliente(dni);
        return "redirect:/clientes";
    }
}
