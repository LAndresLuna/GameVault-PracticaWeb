package com.example.GameVault.controllers;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.service.UsuarioService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(){
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model){

        Usuario usuario = usuarioService.autenticarUsuario(email, password);

        if(usuario != null){
            session.setAttribute("usuario_id", usuario.getId());
            return "redirect:/mis-juegos";
        } else {
            model.addAttribute("error",
                    "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model){
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario){
        usuarioService.registrarUsuario(usuario);
        return "redirect:/login?registrado=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
