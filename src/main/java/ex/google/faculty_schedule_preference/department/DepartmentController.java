package ex.google.faculty_schedule_preference.department;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("department")
public class DepartmentController {
    private final DepartmentRepository repository;

    DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

}
