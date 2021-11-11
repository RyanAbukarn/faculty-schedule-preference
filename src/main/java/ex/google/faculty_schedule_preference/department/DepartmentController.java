package ex.google.faculty_schedule_preference.department;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("department")
public class DepartmentController {
    private final DepartmentRepository repository;

    DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

}
