package ex.google.faculty_schedule_preference.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class home {
    @GetMapping("/")
    public String index() {
        return "home/index";
    }
}
