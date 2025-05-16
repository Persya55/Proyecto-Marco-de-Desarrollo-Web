package com.utp.proyecto.model;

import java.time.LocalDateTime;
import java.util.List;

public class Compra {
    private Long id;
    private Usuario usuario;
    private List<Producto> productos;
    private LocalDateTime fecha;

    public Compra() {
    }

    public Compra(Long id, Usuario usuario, List<Producto> productos, LocalDateTime fecha) {
        this.id = id;
        this.usuario = usuario;
        this.productos = productos;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
