/*
GraficoController es un controlador de Spring MVC encargado de manejar la ruta "/graficos".
Su función principal es preparar y enviar datos estadísticos (productos, reseñas, ventas, categorías)
al modelo para que la vista "graficos.html" pueda mostrar gráficos dinámicos.
  
Actualmente, este controlador no aporta funcionalidad

*/
package com.utp.proyecto.controller;

import com.utp.proyecto.model.Producto;
import com.utp.proyecto.model.Resena;
import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/graficos")
public class GraficoController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    public String graficos(@RequestParam(required = false) Long productoId, Model model) {
        List<Producto> productos = tiendaService.listarProductos();
        List<Resena> resenas = tiendaService.listarResenas();
        List<String> nombres;
        List<Double> promedios;
        List<Long> ventas;
        List<String> categorias = new ArrayList<>();
        List<Long> ventasPorCategoria = new ArrayList<>();
        if (productoId != null) {
            Producto p = tiendaService.buscarProductoPorId(productoId);
            if (p != null) {
                nombres = List.of(p.getNombre());
                List<Resena> r = resenas.stream().filter(x -> x.getProductoId().equals(p.getId())).toList();
                promedios = List
                        .of(r.isEmpty() ? 0.0 : r.stream().mapToInt(Resena::getCalificacion).average().orElse(0.0));
                ventas = List.of(tiendaService.listarCompras().stream().flatMap(c -> c.getProductos().stream())
                        .filter(prod -> prod.getId().equals(p.getId())).count());
                for (String cat : p.getCategorias()) {
                    categorias.add(cat);
                    ventasPorCategoria
                            .add(tiendaService.listarCompras().stream().flatMap(c -> c.getProductos().stream())
                                    .filter(prod -> prod.getCategorias().contains(cat)).count());
                }
            } else {
                nombres = List.of();
                promedios = List.of();
                ventas = List.of();
            }
        } else {
            nombres = productos.stream().map(Producto::getNombre).toList();
            promedios = productos.stream()
                    .map(p -> {
                        List<Resena> r = resenas.stream().filter(x -> x.getProductoId().equals(p.getId())).toList();
                        return r.isEmpty() ? 0.0 : r.stream().mapToInt(Resena::getCalificacion).average().orElse(0.0);
                    }).toList();
            ventas = productos.stream()
                    .map(p -> tiendaService.listarCompras().stream().flatMap(c -> c.getProductos().stream())
                            .filter(prod -> prod.getId().equals(p.getId())).count())
                    .toList();
            for (Producto p : productos) {
                for (String cat : p.getCategorias()) {
                    if (!categorias.contains(cat)) {
                        categorias.add(cat);
                        ventasPorCategoria
                                .add(tiendaService.listarCompras().stream().flatMap(c -> c.getProductos().stream())
                                        .filter(prod -> prod.getCategorias().contains(cat)).count());
                    }
                }
            }
        }
        model.addAttribute("productosLista", productos);
        model.addAttribute("productoId", productoId);
        model.addAttribute("productosGraficos", nombres);
        model.addAttribute("reseñasGraficos", promedios);
        model.addAttribute("ventasGraficos", ventas);
        model.addAttribute("categoriasGraficos", categorias);
        model.addAttribute("ventasPorCategoriaGraficos", ventasPorCategoria);
        return "graficos";
    }
}

 