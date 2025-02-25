package HolaSpring6CV3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;


import HolaSpring6CV3.entity.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Controller
public class AdminController {
    private final String API_URL = "http://localhost:8085/api";  // URL de la API (ajusta según tu configuración)
     @Autowired
    private RestTemplate restTemplate;

    
    @GetMapping("/admin")
    public String adminPage(Authentication authentication,Model model) {
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Roles del usuario: " + authentication.getAuthorities());

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("Acceso concedido: Usuario es admin");

            ResponseEntity<Usuario[]> response = restTemplate.getForEntity(API_URL + "/users", Usuario[].class);
            model.addAttribute("usuarios", response.getBody());
            return "admin";  // Vista para administrador
        } else {
            System.out.println("Acceso denegado: Usuario no es admin");
            return "accessDenied";  // Vista si no es admin
        }
    }

      @PostMapping("/admin/users")
    public String addUser(@ModelAttribute Usuario usuario) {
        // Enviar usuario a la API para ser registrado
        restTemplate.postForObject(API_URL + "/register", usuario, String.class);
        return "redirect:/admin";
    }

    @PostMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        // Lógica para eliminar un usuario a través de la API
        restTemplate.delete(API_URL + "/users/" + id);
        return "redirect:/admin";
    }
}
