package ex.google.faculty_schedule_preference.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/add")
    public String addCourseForm(Model model) {

        Course course = new Course();
        // course.setName("Java 17");
        // System.out.println(course.getName());
        System.out.println(course.getClassType());
        model.addAttribute("course", course);
        model.addAttribute("weekDays", course.getWeekDays().entrySet());
        model.addAttribute("classType", course.getClassType().entrySet());
        model.addAttribute("departments", depRepo.findAll());
        // foreach(Department department : departments)
        // { System.out.println(department.name)}
        //
        //
        return "course/courseForm";
    }

    @PostMapping("/insert")
    public String insertCourse(
            @ModelAttribute("course") Course course,
            @RequestParam("departments") String dept,
            @RequestParam("daysOfWeek") String[] weekDays,
            @RequestParam("classTypes") String classType) {
        Map<Integer, String> newWeekDays = new HashMap<Integer, String>();
        Map<Integer, String> newClassType = Map.of(1, classType);
        int i = 0;
        for (String day : weekDays)
            newWeekDays.put(i++, day);

        Department department = depRepo.findByPrefix(dept);
        course.setDepartment(department);
        course.setWeekDays(newWeekDays);
        course.setClassType(newClassType);
        course.setStatus(1);
        course.setTerm(termRepo.getById((long) 1));
        courseRepo.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{course_id}")
    public String editCourseForm(@PathVariable long course_id, Model model) {
        model.addAttribute("course", courseRepo.findById(course_id).get());
        model.addAttribute("departments", depRepo.findAll());
        return "course/editForm";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable long id, @ModelAttribute("course") Course course,
            @RequestParam("departments") String dept) {

        Department department = depRepo.findByPrefix(dept);
        Course existingCourse = courseRepo.getById(id);
        existingCourse.setId(id);
        // existingCourse.setId(Long.parseLong(String.valueOf(course.getId())));
        existingCourse.setName(course.getName());
        existingCourse.setPrefix(course.getPrefix());
        existingCourse.setType(course.getType());
        existingCourse.setUnit(course.getUnit());
        existingCourse.setDepartment(department);
        existingCourse.setStartTime(course.getStartTime());
        existingCourse.setEndTime(course.getEndTime());
        courseRepo.save(existingCourse);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{course_id}")
    public String deleteCourse(@PathVariable long course_id) {
        courseRepo.deleteById(course_id);
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
