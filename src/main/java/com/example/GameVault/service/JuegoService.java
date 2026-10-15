package com.example.GameVault.service;

import com.example.GameVault.model.Juego;
import com.example.GameVault.repository.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class JuegoService {

    @Autowired
    JuegoRepository juegoRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    public List<Juego> ObtenerTodos(){
        return juegoRepository.findAll();
    }

    public void guardarJuego(Juego juego, MultipartFile portada){
        String urlImage = juego.getPortadaUrl() != null ?
                juego.getPortadaUrl() : "default.png";

        if(!portada.isEmpty()){
            urlImage = guardarImagenLocal(portada);
        }



        juego.setPortadaUrl(urlImage);
        juegoRepository.save(juego);
    }

    public Juego obtenerJuegoPorId(Long id){
        return juegoRepository.findById(id).orElse(null);
    }

    public void eliminarJuego(Long id){
        juegoRepository.deleteById(id);
    }

    public List<Juego> obtenerJuegoPorUsuario(Long usuarioId){
        return juegoRepository.findByUsuarioId(usuarioId);
    }

    public String guardarImagenLocal(MultipartFile portada){
        try {
            // Tip para clase: Crear la carpeta si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Genera un nombre único para evitar sobreescribir archivos con el mismo nombre
            String nombreArchivo = UUID.randomUUID().toString() + "_" + portada.getOriginalFilename();
            Path filePath = uploadPath.resolve(nombreArchivo);

            // Guardamos el archivo fisicamente en disco
            Files.copy(portada.getInputStream(), filePath);

            return nombreArchivo;

        } catch (IOException e) {
            e.printStackTrace();
            return "default.png";
        }
    }

    public List<Juego> buscarPorTitulo(String palabra) {
        if (palabra == null || palabra.trim().isEmpty()) {
            return ObtenerTodos();
        }

        return juegoRepository.findByTituloContainingIgnoreCase(palabra);
    }

}
