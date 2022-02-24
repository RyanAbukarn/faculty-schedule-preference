package ex.google.faculty_schedule_preference.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;

@Controller
public class CourseController {
    private final CourseRepository repository;

    CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private DepartmentRepository depRepo;

    @GetMapping("/courses")
    public String index(Model model) {
        model.addAttribute("courses", repository.findAll());
        return "course/index";
    }

    @GetMapping("/add/course")
    public String addCourseForm(Model model) {

        Course course = new Course();
        // course.setName("Java 17");
        // System.out.println(course.getName());
        model.addAttribute("course", course);
        model.addAttribute("departments", depRepo.findAll());

        return "course/courseForm";
    }

    @PostMapping("/insert/courses")
    public String insertCourse(@ModelAttribute("course") Course course, @RequestParam("departments") String dept) {
        System.out.println(course.toString());
        Department department = depRepo.findByPrefix(dept);
        course.setDepartment(department);
        repository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/course/{course_id}")
    public String editCourseForm(@PathVariable long course_id, Model model) {
        model.addAttribute("course", repository.findById(course_id).get());
        model.addAttribute("departments", depRepo.findAll());
        return "course/editForm";
    }

    @PostMapping("/update/courses/{id}")
    public String updateCourse(@PathVariable long id, @ModelAttribute("course") Course course,
            @RequestParam("departments") String dept) {

        Department department = depRepo.findByPrefix(dept);
        Course existingCourse = repository.getById(id);
        existingCourse.setId(id);
        // existingCourse.setId(Long.parseLong(String.valueOf(course.getId())));
        existingCourse.setName(course.getName());
        existingCourse.setPrefix(course.getPrefix());
        existingCourse.setType(course.getType());
        existingCourse.setUnit(course.getUnit());
        existingCourse.setDepartment(department);
        existingCourse.setStartTime(course.getStartTime());
        existingCourse.setEndTime(course.getEndTime());
        repository.save(existingCourse);
        return "redirect:/courses";
    }

    @GetMapping("/delete/course/{course_id}")
    public String deleteCourse(@PathVariable long course_id, Model model) {
        repository.deleteById(course_id);
        return "redirect:/courses";
    }

}
