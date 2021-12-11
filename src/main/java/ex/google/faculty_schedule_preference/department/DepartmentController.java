package ex.google.faculty_schedule_preference.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class DepartmentController {

    @Autowired
    private DepartmentRepository repository;

    DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("departments")
    public String index(Model model) {
        model.addAttribute("departments", repository.findAll());
        return "department/index";
    }

    @GetMapping("test_styles")
    public String html_test(Model model) {
        return "test_styles";
    }
}
