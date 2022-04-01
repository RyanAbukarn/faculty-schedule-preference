package ex.google.faculty_schedule_preference.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import ex.google.faculty_schedule_preference.term.Term;
import ex.google.faculty_schedule_preference.term.TermRepository;

@Controller
@RequestMapping("courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private DepartmentRepository depRepo;

    @Autowired
    private TermRepository termRepo;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("departments", depRepo.findAll());
        model.addAttribute("search", "true");
        return "course/index";
    }

    @GetMapping("/new")
    public String new_(Model model) {

        Course course = new Course();
        model.addAttribute("course", course);
        model.addAttribute("departments", depRepo.findAll());
        model.addAttribute("terms", termRepo.findAll());

        return "course/new";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute("course") Course course,
            @RequestParam("department_id") String departmentID,
            @RequestParam("term_id") Long termID) {
        Long deptId = Long.parseLong(departmentID.split(":")[0]);
        Department department = depRepo.findById(deptId).get();
        Term term = termRepo.findById(termID).get();
        course.setTerm(term);
        course.setDepartment(department);
        courseRepo.save(course);
        return "redirect:/courses";
    }

    @GetMapping("{courseID}/edit")
    public String edit(@PathVariable long courseID, Model model) {
        Course course = courseRepo.findById(courseID).get();
        model.addAttribute("course", course);
        model.addAttribute("departments", depRepo.findAll());
        return "course/edit";
    }

    @PostMapping("{courseID}/update")
    public String update(@PathVariable long courseID, @ModelAttribute("course") Course requestCourse,
            @RequestParam("department_id") String departmentID) {
        Long deptID = Long.parseLong(departmentID.split(":")[0]);
        Department department = depRepo.findById(deptID).get();
        Course course = courseRepo.getById(courseID);
        course.setName(requestCourse.getName());
        course.setPrefix(requestCourse.getPrefix());
        course.setUnit(requestCourse.getUnit());
        course.setDepartment(department);
        courseRepo.save(course);
        return "redirect:/courses";
    }

    // searching on the base of department
    @PostMapping("/search")
    public String searchByDepartment(@RequestParam("departments") String dept, Model model) {
        Department department = depRepo.findByPrefix(dept);
        List<Course> courseList = courseRepo.getCoursesByDepartment(department.getId());
        model.addAttribute("search", "false");
        model.addAttribute("courses", courseList);
        return "course/index";
    }

}
