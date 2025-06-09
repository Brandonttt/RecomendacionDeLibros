package HolaSpring6CV3.controller;

import HolaSpring6CV3.entity.LibroFavorito;
import HolaSpring6CV3.service.LibroFavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/libros")
public class ApiLibroController {

    @Autowired
    private LibroFavoritoService libroFavoritoService;

    @GetMapping("/favoritos")
    public ResponseEntity<List<LibroFavorito>> obtenerFavoritos() {
        return ResponseEntity.ok(libroFavoritoService.obtenerFavoritosUsuarioActual());
    }

    @PostMapping("/favoritos/agregar")
    public ResponseEntity<Map<String, Object>> agregarFavorito(
            @RequestParam String libroId,
            @RequestParam String titulo,
            @RequestParam String autor,
            @RequestParam String imagenUrl) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            libroFavoritoService.agregarFavorito(libroId, titulo, autor, imagenUrl);
            response.put("favorito", true);
            response.put("mensaje", "Libro agregado a favoritos");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("favorito", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/favoritos/eliminar/{libroId}")
    public ResponseEntity<Map<String, Object>> eliminarFavorito(@PathVariable String libroId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            libroFavoritoService.eliminarFavorito(libroId);
            response.put("mensaje", "Libro eliminado de favoritos");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/favoritos/estado/{libroId}")
    public ResponseEntity<Map<String, Object>> verificarFavorito(@PathVariable String libroId) {
        Map<String, Object> response = new HashMap<>();
        response.put("esFavorito", libroFavoritoService.esFavorito(libroId));
        return ResponseEntity.ok(response);
    }
}