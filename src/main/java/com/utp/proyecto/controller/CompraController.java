package com.utp.proyecto.controller;

import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    public String compras(@RequestParam(required = false) Long usuarioId, Model model) {
        if (usuarioId != null) {
            model.addAttribute("compras", tiendaService.buscarComprasPorUsuario(usuarioId));
        } else {
            model.addAttribute("compras", tiendaService.listarCompras());
        }
        return "compras";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCompra(@PathVariable Long id) {
        tiendaService.eliminarCompra(id);
        return "redirect:/compras";
    }
}
