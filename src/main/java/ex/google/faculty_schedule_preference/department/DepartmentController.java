package ex.google.faculty_schedule_preference.department;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller

@RequestMapping("department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository repository;

    DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/departments")
    public String fetchDepartments(Model model)
    {
        List<Department> departments = new ArrayList<Department>();
        repository.findAll()
                        .forEach(departments::add);

        model.addAttribute("departments", departments);
        return "department/list";
    }

}
