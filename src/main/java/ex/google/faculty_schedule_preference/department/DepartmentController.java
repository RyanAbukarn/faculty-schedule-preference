package ex.google.faculty_schedule_preference.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository repository;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("departments", repository.findAll());
        return "department/index";
    }
}
