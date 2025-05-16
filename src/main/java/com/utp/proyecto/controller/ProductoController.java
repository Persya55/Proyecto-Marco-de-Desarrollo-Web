package com.utp.proyecto.controller;

import com.utp.proyecto.model.Producto;
import com.utp.proyecto.model.Resena;
import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    public String productos(Model model) {
        model.addAttribute("productos", tiendaService.listarProductos());
        model.addAttribute("producto", new Producto());
        return "productos";
    }

    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam String nombre,
                                  @RequestParam String categorias,
                                  @RequestParam double precio,
                                  @RequestParam int stock,
                                  @RequestParam(required = false) String imagenUrl) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setCategorias(
                Arrays.stream(categorias.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList());
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagenUrl(imagenUrl);
        tiendaService.agregarProducto(producto);
        return "redirect:/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        tiendaService.eliminarProducto(id);
        return "redirect:/productos";
    }

    @GetMapping("/buscar")
    public String buscarProducto(@RequestParam String nombre, Model model) {
        model.addAttribute("productos", tiendaService.buscarProductosPorNombre(nombre));
        model.addAttribute("producto", new Producto());
        return "productos";
    }

    @GetMapping("/{id}")
    public String detalleProducto(@PathVariable Long id, Model model) {
        Producto producto = tiendaService.buscarProductoPorId(id);
        if (producto == null) {
            return "redirect:/buscar_juegos";
        }
        model.addAttribute("producto", producto);
        model.addAttribute("resenas", tiendaService.listarResenasPorProducto(id));
        List<Producto> relacionados = tiendaService.listarProductos().stream()
                .filter(p -> !p.getId().equals(producto.getId()) && p.getCategorias() != null
                        && producto.getCategorias() != null
                        && p.getCategorias().stream().anyMatch(producto.getCategorias()::contains))
                .limit(3)
                .toList();
        model.addAttribute("relacionados", relacionados);
        return "detalle_producto";
    }

    @PostMapping("/{id}/reseña")
    public String agregarResena(@PathVariable Long id, @RequestParam String comentario, @RequestParam int calificacion,
                                @RequestParam(required = false) String usuario) {
        if (calificacion < 1)
            calificacion = 1;
        if (calificacion > 5)
            calificacion = 5;
        Resena resena = new Resena();
        resena.setProductoId(id);
        resena.setComentario(comentario);
        resena.setCalificacion(calificacion);
        resena.setUsuario(usuario != null && !usuario.isBlank() ? usuario : "Anónimo");
        tiendaService.agregarResena(resena);
        return "redirect:/productos/" + id;
    }
}
