package ex.google.faculty_schedule_preference.request;

import org.hibernate.secure.spi.PermissibleAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Controller;

import ex.google.faculty_schedule_preference.course.CourseRepository;
import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import ex.google.faculty_schedule_preference.permission.PermissionRepository;
import ex.google.faculty_schedule_preference.user.User;
import ex.google.faculty_schedule_preference.user.UserRepository;

@Controller
public class RequestController {
    @Autowired
    private RequestRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping("requests")
    public String Index(Model model) {
        model.addAttribute("request", "");
        return "request";
    }

    @GetMapping("requests/{request_id}")
    public String View(@PathVariable("request_id") long request_id, Model model) {
        model.addAttribute("request", "");
        return "/request/view";
    }

    @GetMapping("courses/{course_id}/request")
    public String Create(@PathVariable("course_id") long course_id, Model model) {
        model.addAttribute("request", new Request());
        return "/request/create";
    }

}
