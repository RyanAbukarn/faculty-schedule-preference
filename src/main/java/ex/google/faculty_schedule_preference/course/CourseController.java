package ex.google.faculty_schedule_preference.course;

import java.util.HashMap;
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
        model.addAttribute("weekDays", Course.weekDays);
        model.addAttribute("classType", Course.classType);
        model.addAttribute("departments", depRepo.findAll());
        model.addAttribute("terms", termRepo.findAll());

        return "course/new";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute("course") Course course,
            @RequestParam("department_id") Long departmentID,
            @RequestParam("term_id") Long termID) {
        Department department = depRepo.findById(departmentID).get();
        Term term = termRepo.findById(termID).get();
        course.setTerm(term);
        course.setDepartment(department);
        course.setWeekSchedule(course.getWeekSchedule());
        course.setType(course.getType());
        course.setStatus(1);
        courseRepo.save(course);
        return "redirect:/courses";
    }

    @GetMapping("{course_id}/edit")
    public String edit(@PathVariable long course_id, Model model) {
        Course course = courseRepo.findById(course_id).get();

        HashMap<String, Boolean> courseWeekDays = new HashMap<String, Boolean>();

        for (String day : course.getWeekSchedule().split(","))
            courseWeekDays.put(day, true);

        model.addAttribute("course", course);
        model.addAttribute("weekDays", Course.weekDays);
        model.addAttribute("courseWeekDays", courseWeekDays);
        model.addAttribute("types", Course.classType);

        model.addAttribute("departments", depRepo.findAll());
        return "course/edit";
    }

    @PostMapping("{course_id}/update")
    public String update(@PathVariable long course_id, @ModelAttribute("course") Course requestCourse,
            @RequestParam("department_id") long dept_id) {

        Department department = depRepo.findById(dept_id).get();
        Course course = courseRepo.getById(course_id);
        course.setName(requestCourse.getName());
        course.setPrefix(requestCourse.getPrefix());
        course.setType(requestCourse.getType());
        course.setUnit(requestCourse.getUnit());
        course.setWeekSchedule(requestCourse.getWeekSchedule());
        course.setDepartment(department);
        course.setStartTime(requestCourse.getStartTime());
        course.setEndTime(requestCourse.getEndTime());
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
