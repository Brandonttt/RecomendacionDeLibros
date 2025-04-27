package HolaSpring6CV3.controller;

import HolaSpring6CV3.entity.LibroFavorito;
import HolaSpring6CV3.service.LibroFavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/favoritos")
public class LibroFavoritoController {

    @Autowired
    private LibroFavoritoService libroFavoritoService;

    /**
     * Muestra la página de favoritos.
     */
    @GetMapping
    public String mostrarFavoritos(Model model) {
        List<LibroFavorito> favoritos = libroFavoritoService.obtenerFavoritosUsuarioActual();
        model.addAttribute("favoritos", favoritos);
        return "favoritos"; // Nombre del archivo favoritos.html
    }

    /**
     * Obtiene todos los libros favoritos del usuario actual (API REST).
     */
    @GetMapping("/api")
    @ResponseBody
    public List<LibroFavorito> obtenerFavoritos() {
        return libroFavoritoService.obtenerFavoritosUsuarioActual();
    }

    /**
     * Agrega un libro a favoritos (API REST).
     */
    @PostMapping("/api/agregar")
    @ResponseBody
    public String agregarFavorito(@RequestParam String libroId,
                                   @RequestParam String titulo,
                                   @RequestParam(required = false) String autor,
                                   @RequestParam(required = false) String imagenUrl) {
        try {
            libroFavoritoService.agregarFavorito(libroId, titulo, autor, imagenUrl);
            return "Libro agregado a favoritos correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Elimina un libro de favoritos (API REST).
     */
    @DeleteMapping("/api/eliminar/{libroId}")
    @ResponseBody
    public String eliminarFavorito(@PathVariable String libroId) {
        try {
            libroFavoritoService.eliminarFavorito(libroId);
            return "Libro eliminado de favoritos correctamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Verifica si un libro está en favoritos (API REST).
     */
    @GetMapping("/api/esFavorito/{libroId}")
    @ResponseBody
    public boolean esFavorito(@PathVariable String libroId) {
        return libroFavoritoService.esFavorito(libroId);
    }
}