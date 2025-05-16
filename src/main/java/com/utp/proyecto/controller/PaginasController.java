package com.utp.proyecto.controller;

import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaginasController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping("/buscar_juegos")
    public String buscarJuegos(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("search", search);
        model.addAttribute("productos", tiendaService.listarProductos());
        return "buscar_juegos";
    }

    @GetMapping("/minecraft")
    public String minecraft() {
        return "minecraft";
    }
}
