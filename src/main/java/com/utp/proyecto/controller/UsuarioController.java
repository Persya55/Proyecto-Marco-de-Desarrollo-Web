package com.utp.proyecto.controller;

import com.utp.proyecto.model.Usuario;
import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    public String usuarios(Model model) {
        model.addAttribute("usuarios", tiendaService.listarUsuarios());
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }

    @PostMapping("/agregar")
    public String agregarUsuario(@RequestParam String usuario,
                                 @RequestParam String nombre,
                                 @RequestParam String email,
                                 @RequestParam(required = false) String password,
                                 @RequestParam(defaultValue = "false") boolean admin) {
        Usuario u = new Usuario();
        u.setUsuario(usuario);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword(password);
        u.setAdmin(admin);
        tiendaService.agregarUsuario(u);
        return "redirect:/usuarios";
    }

    @PostMapping("/actualizarAdmin")
    public String actualizarAdmin(@RequestParam(value = "adminIds", required = false) List<Long> adminIds) {
        List<Usuario> usuarios = tiendaService.listarUsuarios();
        for (Usuario u : usuarios) {
            u.setAdmin(adminIds != null && adminIds.contains(u.getId()));
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/registrar")
    @ResponseBody
    public String registrarUsuario(@RequestParam String usuario,
                                   @RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String password) {
        Usuario u = new Usuario();
        u.setUsuario(usuario);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword(password);
        u.setAdmin(false);
        tiendaService.agregarUsuario(u);
        return "ok";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        tiendaService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/buscar")
    public String buscarUsuario(@RequestParam String nombre, Model model) {
        List<Usuario> usuariosFiltrados = tiendaService.listarUsuarios().stream()
            .filter(u -> u.getNombre() != null && u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .toList();
        model.addAttribute("usuarios", usuariosFiltrados);
        model.addAttribute("usuario", new Usuario());
        return "usuarios";
    }
}
