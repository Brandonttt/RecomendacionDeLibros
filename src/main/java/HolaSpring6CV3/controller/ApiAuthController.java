package HolaSpring6CV3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import HolaSpring6CV3.entity.Rol;
import HolaSpring6CV3.entity.Usuario;
import HolaSpring6CV3.repository.UsuarioRepository;
import HolaSpring6CV3.service.CustomUserDetailsService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")  // Cambiado a /api/auth para que coincida con el cliente Flutter
public class ApiAuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @GetMapping("/ping")
    public ResponseEntity<Map<String, Object>> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "API is running!");
        return ResponseEntity.ok(response);
    }

    // Clase interna para manejar las credenciales de login
    public static class LoginCredentials {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        try {
            // Intentamos autenticar con el nombre de usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
            );

            // Si la autenticación fue exitosa, devolvemos los datos del usuario
            Usuario usuario = userRepository.findByNombre(credentials.getUsername());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("username", usuario.getNombre());
            response.put("email", usuario.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Credenciales inválidas");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Clase interna para manejar el registro
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Verificar si el email ya está en uso
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                response.put("success", false);
                response.put("error", "El correo ya está en uso");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Verificar si el nombre de usuario ya existe
            if (userRepository.findByNombre(registerRequest.getUsername()) != null) {
                response.put("success", false);
                response.put("error", "El nombre de usuario ya está en uso");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            // Crear el nuevo usuario
            Usuario user = new Usuario();
            user.setNombre(registerRequest.getUsername());
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            
            // Asignar un rol por defecto si es necesario
            // Aquí deberías agregar código para asignar el rol de usuario normal
            
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/users")
    public ResponseEntity<Iterable<Usuario>> getAllUsers() {
        Iterable<Usuario> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users/{id}/toggle-admin")
    public ResponseEntity<?> toggleAdminRole(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        return userRepository.findById(id).map(user -> {
            boolean isAdmin = user.getRoles().stream()
                .anyMatch(rol -> rol.getNombre().equals("ROLE_ADMIN"));
            
            Rol adminRole = new Rol();
            adminRole.setNombre("ROLE_ADMIN");

            if (isAdmin) {
                user.getRoles().removeIf(rol -> rol.getNombre().equals("ROLE_ADMIN"));
                response.put("adminRole", false);
            } else {
                user.getRoles().add(adminRole);
                response.put("adminRole", true);
            }

            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Rol de admin actualizado");
            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of("success", false, "error", "Usuario no encontrado")
        ));
    }
}