package com.utp.proyecto.controller;

import com.utp.proyecto.model.Producto;
import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    public String carrito(@RequestParam(required = false) Long usuarioId, Model model) {
        if (usuarioId == null) {
            return "carrito";
        }
        model.addAttribute("carrito", tiendaService.getCarrito(usuarioId));
        model.addAttribute("usuarioId", usuarioId);
        return "carrito";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long usuarioId, @RequestParam Long productoId) {
        Producto p = tiendaService.buscarProductoPorId(productoId);
        if (p != null)
            tiendaService.agregarAlCarrito(usuarioId, p);
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }

    @GetMapping("/eliminar/{usuarioId}/{productoId}")
    public String eliminarDelCarrito(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        tiendaService.eliminarDelCarrito(usuarioId, productoId);
        return "redirect:/carrito?usuarioId=" + usuarioId;
    }

    @PostMapping("/comprar")
    public String comprar(@RequestParam Long usuarioId, @RequestParam(required = false) String productos) {
        if (productos != null && !productos.isEmpty()) {
            String[] ids = productos.split(",");
            List<Long> productoIds = new ArrayList<>();
            for (String id : ids) {
                try {
                    productoIds.add(Long.parseLong(id));
                } catch (NumberFormatException ignored) {
                }
            }
            tiendaService.registrarCompraConProductos(usuarioId, productoIds);
        } else {
            tiendaService.registrarCompra(usuarioId);
        }
        return "redirect:/compras?usuarioId=" + usuarioId;
    }
}
