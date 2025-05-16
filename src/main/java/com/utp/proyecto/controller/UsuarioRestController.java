package com.utp.proyecto.controller;

import com.utp.proyecto.model.Usuario;
import com.utp.proyecto.service.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
    @Autowired
    private TiendaService tiendaService;

    @GetMapping("/rol")
    public Map<String, Object> getRol(@RequestParam String usuario) {
        Usuario user = buscarPorUsuario(tiendaService, usuario);
        Map<String, Object> result = new HashMap<>();
        result.put("admin", user != null && user.isAdmin());
        return result;
    }

    @GetMapping("/id")
    public Map<String, Object> getId(@RequestParam String usuario) {
        Usuario user = buscarPorUsuario(tiendaService, usuario);
        Map<String, Object> result = new HashMap<>();
        result.put("id", user != null ? user.getId() : null);
        return result;
    }

    private Usuario buscarPorUsuario(TiendaService tiendaService, String usuario) {
        for (Usuario u : tiendaService.listarUsuarios()) {
            if (u.getUsuario() != null && u.getUsuario().equalsIgnoreCase(usuario)) {
                return u;
            }
        }
        return null;
    }
}