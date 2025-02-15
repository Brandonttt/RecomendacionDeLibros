package HolaSpring6CV3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/") // Define la ruta base
public class HolaController {

    @GetMapping
    public String holaMundo() {
        return "Hola Mundo desde Spring gfdgdfgdgd!";
        
    }
}