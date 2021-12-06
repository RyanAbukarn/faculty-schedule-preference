package ex.google.faculty_schedule_preference.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CourseController {
    private final CourseRepository repository;

    CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("courses")
    public String index(Model model) {
        model.addAttribute("courses", repository.findAll());
        return "course/index";
    }

}
