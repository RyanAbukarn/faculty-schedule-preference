package ex.google.faculty_schedule_preference.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome</h1>");
    }

    @GetMapping("/lecturer")
    public String lecturer() {
        return ("<h1>Welcome lecturer</h1>");
    }

    @GetMapping("/tenurertrack")
    public String tenurertrack() {
        return ("<h1>Welcome tenure track</h1>");
    }

    @GetMapping("/controller")
    public String controller() {
        return ("<h1>Welcome Controller</h1>");
    }


    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }
}
