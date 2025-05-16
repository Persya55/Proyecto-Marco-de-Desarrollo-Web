package com.utp.proyecto.model;

public class Resena {
    private Long id;
    private Long productoId;
    private String usuario;
    private String comentario;
    private int calificacion;
    public Resena() {
    }

    public Resena(Long id, Long productoId, String usuario, String comentario, int calificacion) {
        this.id = id;
        this.productoId = productoId;
        this.usuario = usuario;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
