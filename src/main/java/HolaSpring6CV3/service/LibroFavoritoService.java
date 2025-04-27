package HolaSpring6CV3.service;

import HolaSpring6CV3.entity.LibroFavorito;
 import HolaSpring6CV3.entity.Usuario;
 import HolaSpring6CV3.repository.LibroFavoritoRepository;
 import HolaSpring6CV3.repository.UsuarioRepository;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.security.core.Authentication;
 import org.springframework.security.core.context.SecurityContextHolder;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 
 import java.util.List;
 import java.time.LocalDateTime;
 
 @Service
 public class LibroFavoritoService {
 
     @Autowired
     private LibroFavoritoRepository libroFavoritoRepository;
 
     @Autowired
     private UsuarioRepository usuarioRepository;
 
     /**
      * Obtiene el usuario actual autenticado
      */
     public Usuario getUsuarioActual() {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String username = auth.getName();
         return usuarioRepository.findByNombre(username);
     }
 
     /**
      * Obtiene todos los libros favoritos del usuario actual
      */
     public List<LibroFavorito> obtenerFavoritosUsuarioActual() {
         Usuario usuario = getUsuarioActual();
         if (usuario == null) {
             throw new RuntimeException("Usuario no encontrado");
         }
         return libroFavoritoRepository.findByUsuarioOrderByFechaAgregadoDesc(usuario);
     }
 
     /**
      * Agrega un libro a favoritos
      */
      public void agregarFavorito(String libroId, String titulo, String autor, String imagenUrl) {
        Usuario usuario = getUsuarioActual(); // Método para obtener el usuario actual
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
    
        if (libroFavoritoRepository.existsByUsuarioAndLibroId(usuario, libroId)) {
            throw new RuntimeException("El libro ya está en favoritos.");
        }
    
        LibroFavorito favorito = new LibroFavorito();
        favorito.setUsuario(usuario); // Asignar el usuario actual
        favorito.setLibroId(libroId);
        favorito.setTitulo(titulo);
        favorito.setAutor(autor);
        favorito.setImagenUrl(imagenUrl);
        favorito.setFechaAgregado(LocalDateTime.now());
    
        libroFavoritoRepository.save(favorito);
    }
 
     /**
      * Elimina un libro de favoritos
      */
     @Transactional
     public void eliminarFavorito(String libroId) {
         Usuario usuario = getUsuarioActual();
         if (usuario == null) {
             throw new RuntimeException("Usuario no encontrado");
         }
 
         libroFavoritoRepository.findByUsuarioAndLibroId(usuario, libroId)
                 .ifPresent(favorito -> libroFavoritoRepository.delete(favorito));
     }
 
     /**
      * Verifica si un libro está en favoritos
      */
     public boolean esFavorito(String libroId) {
         Usuario usuario = getUsuarioActual();
         if (usuario == null) {
             return false;
         }
         return libroFavoritoRepository.existsByUsuarioAndLibroId(usuario, libroId);
     }
 }