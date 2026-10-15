package com.example.GameVault.controllers;

import com.example.GameVault.model.Juego;
import com.example.GameVault.model.Usuario;
import com.example.GameVault.service.JuegoService;
import com.example.GameVault.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GameController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/fragments-demo")
    public String fragmentsDemo() {
        return "fragments-demo";
    }

    @GetMapping({"/", "/juegos"})
    public String listarJuegos(
            @RequestParam(name = "buscar", required = false) String buscar,
            Model model) {

        if (buscar != null && !buscar.isEmpty()) {
            model.addAttribute("juegos", juegoService.buscarPorTitulo(buscar));
            model.addAttribute("busquedaActual", buscar);
        } else {
            model.addAttribute("juegos", juegoService.ObtenerTodos());
        }

        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(Model model) {

        model.addAttribute("juego", new Juego());

        return "formulario";
    }

    @GetMapping("/mis-juegos")
    public String dashboard(Model model){
        Usuario logueado = usuarioService.obtenerUsuarioLogeado();

        model.addAttribute("juegos", juegoService.obtenerJuegoPorUsuario(logueado.getId()));
        model.addAttribute("usuario", logueado);

        return "mis-juegos";
    }

    @PostMapping("/juegos")
    public String guardarJuego(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("categoria") String categoria,
            @RequestParam("portada") MultipartFile portada)
            {

        Juego nuevoJuego = new Juego();
        nuevoJuego.setTitulo(titulo);
        nuevoJuego.setDescripcion(descripcion);
        nuevoJuego.setPrecio(precio);
        nuevoJuego.setCategoria(categoria);

        nuevoJuego.setUsuario(usuarioService.obtenerUsuarioLogeado());

        juegoService.guardarJuego(nuevoJuego, portada);

        return "redirect:/juegos";
    }
}