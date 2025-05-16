package com.utp.proyecto.service;

import com.utp.proyecto.model.Producto;
import com.utp.proyecto.model.Usuario;
import com.utp.proyecto.model.Compra;
import com.utp.proyecto.model.Resena;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TiendaService {
    private final List<Producto> productos = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final Map<Long, List<Producto>> carritos = new HashMap<>();
    private final List<Resena> resenas = new ArrayList<>();
    private final AtomicLong productoId = new AtomicLong(1);
    private final AtomicLong usuarioId = new AtomicLong(1);
    private final AtomicLong compraId = new AtomicLong(1);
    private final AtomicLong resenaId = new AtomicLong(1);

    public TiendaService() {
        agregarProducto(crearProductoEjemplo("Persona 3 Reload", List.of("RPG", "PlayStation"), 59.99, 10,
                "https://rimage.ripley.com.pe/home.ripley/Attachment/MKP/2239/PMP20000250828/full_image-1.jpeg"));
        agregarProducto(crearProductoEjemplo("Elden Ring", List.of("RPG", "PlayStation"), 59.99, 10,
                "https://m.media-amazon.com/images/I/81mtrRvlFqL._AC_SL1500_.jpg"));
        agregarProducto(crearProductoEjemplo("Super Mario Bros Wonder", List.of("Plataformas", "Nintendo"), 49.99, 10,
                "https://m.media-amazon.com/images/I/51MI1riPckL.jpg"));
        agregarProducto(crearProductoEjemplo("The Legend of Zelda: Breath Of the Wild (edicion coleccionista)",
                List.of("Aventura", "Nintendo"), 59.99, 10,
                "https://m.media-amazon.com/images/I/810qImFqrkL._AC_UF894,1000_QL80_.jpg"));
        agregarProducto(crearProductoEjemplo("Super Smash Bros: Ultimate", List.of("Lucha", "Nintendo"), 59.99, 10,
                "https://http2.mlstatic.com/D_NQ_NP_646016-MLA45733755264_042021-O.webp"));
        agregarProducto(crearProductoEjemplo("Astro Bot", List.of("Plataformas", "PlayStation"), 39.99, 10,
                "https://m.media-amazon.com/images/I/81M-NPlVi9L._SL1500_.jpg"));
        agregarProducto(crearProductoEjemplo("Spiderman: Miles Morales", List.of("Acción", "PlayStation"), 69.99, 10,
                "https://m.media-amazon.com/images/I/71BWFLW-uxL._SL1361_.jpg"));
        agregarProducto(crearProductoEjemplo("The Binding of Isaac Repentance", List.of("Roguelike", "PlayStation"),
                34.99, 10, "https://m.media-amazon.com/images/I/812ecJBMR1L._SL1500_.jpg"));
        agregarProducto(
                crearProductoEjemplo("Monster Hunter Wilds: Steelbox edition", List.of("Aventura", "PlayStation"),
                        59.99, 10, "https://m.media-amazon.com/images/I/81j+Pljm64L._SL1500_.jpg"));
        agregarProducto(crearProductoEjemplo("Minecraft", List.of("Aventura", "PlayStation"), 29.99, 10,
                "https://http2.mlstatic.com/D_NQ_NP_684042-MLU72699956877_112023-O.webp"));
    }

    private Producto crearProductoEjemplo(String nombre, List<String> categorias, double precio, int stock,
            String imagenUrl) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setCategorias(categorias);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setImagenUrl(imagenUrl);
        return p;
    }

    public Producto agregarProducto(Producto p) {
        p.setId(productoId.getAndIncrement());
        productos.add(p);
        return p;
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos);
    }

    public Producto buscarProductoPorId(Long id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productos.stream().filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void eliminarProducto(Long id) {
        productos.removeIf(p -> p.getId().equals(id));
    }

    public Usuario agregarUsuario(Usuario u) {
        u.setId(usuarioId.getAndIncrement());
        if (u.getUsuario() == null)
            u.setUsuario(u.getNombre());
        usuarios.add(u);
        return u;
    }

    public void eliminarUsuario(Long id) {
        usuarios.removeIf(u -> u.getId().equals(id));
    }

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public Usuario buscarUsuarioPorNombre(String nombre) {
        return usuarios.stream().filter(u -> u.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);
    }

    public List<Producto> getCarrito(Long usuarioId) {
        return carritos.getOrDefault(usuarioId, new ArrayList<>());
    }

    public void agregarAlCarrito(Long usuarioId, Producto producto) {
        carritos.computeIfAbsent(usuarioId, k -> new ArrayList<>()).add(producto);
    }

    public void eliminarDelCarrito(Long usuarioId, Long productoId) {
        List<Producto> carrito = carritos.get(usuarioId);
        if (carrito != null) {
            carrito.removeIf(p -> p.getId().equals(productoId));
        }
    }

    public void vaciarCarrito(Long usuarioId) {
        carritos.remove(usuarioId);
    }

    public Compra registrarCompra(Long usuarioId) {
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        List<Producto> carrito = getCarrito(usuarioId);
        if (usuario != null && carrito != null && !carrito.isEmpty()) {
            Compra compra = new Compra(compraId.getAndIncrement(), usuario, new ArrayList<>(carrito),
                    java.time.LocalDateTime.now());
            compras.add(compra);
            vaciarCarrito(usuarioId);
            return compra;
        }
        return null;
    }

    public Compra registrarCompraConProductos(Long usuarioId, List<Long> productoIds) {
        Usuario usuario = buscarUsuarioPorId(usuarioId);
        List<Producto> productos = new ArrayList<>();
        for (Long id : productoIds) {
            Producto p = buscarProductoPorId(id);
            if (p != null)
                productos.add(p);
        }
        if (usuario != null && !productos.isEmpty()) {
            Compra compra = new Compra(compraId.getAndIncrement(), usuario, productos, java.time.LocalDateTime.now());
            compras.add(compra);
            vaciarCarrito(usuarioId);
            return compra;
        }
        return null;
    }

    public List<Compra> listarCompras() {
        return new ArrayList<>(compras);
    }

    public List<Compra> buscarComprasPorUsuario(Long usuarioId) {
        return compras.stream().filter(c -> c.getUsuario().getId().equals(usuarioId)).collect(Collectors.toList());
    }

    public Compra buscarCompraPorId(Long id) {
        return compras.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    public void eliminarCompra(Long id) {
        compras.removeIf(c -> c.getId().equals(id));
    }

    public Resena agregarResena(Resena r) {
        r.setId(resenaId.getAndIncrement());
        resenas.add(r);
        return r;
    }

    public List<Resena> listarResenasPorProducto(Long productoId) {
        return resenas.stream().filter(r -> r.getProductoId().equals(productoId)).toList();
    }

    public List<Resena> listarResenas() {
        return new ArrayList<>(resenas);
    }

}
