package HolaSpring6CV3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and() // Habilita CORS - AÑADE ESTA LÍNEA AQUÍ
            .csrf().disable()  // Desactiva CSRF para facilitar el acceso desde la aplicación móvil
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/auth/**").permitAll()  // Permitir acceso sin autenticación a la app móvil
                .requestMatchers("/api/libros/**").permitAll() // Permitir acceso a la API de libros
                .requestMatchers("/admin").hasRole("ADMIN")  // Acceso solo para usuarios con rol ADMIN a la ruta /admin
                .requestMatchers("/perfil").authenticated()
                .requestMatchers("/home").authenticated()  // Requiere autenticación para acceder a /home
                .anyRequest().permitAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")  // Mantiene el form login para el navegador
                .defaultSuccessUrl("/home", true)  // Redirige a /home tras login exitoso
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }

    // Configuración de CORS - AÑADE ESTE MÉTODO
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permite todos los orígenes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(false); // Cambia a true si necesitas enviar cookies
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}