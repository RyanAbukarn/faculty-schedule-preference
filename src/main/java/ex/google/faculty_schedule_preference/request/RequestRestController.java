package ex.google.faculty_schedule_preference.request;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.registry.infomodel.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ex.google.faculty_schedule_preference.course.Course;
import ex.google.faculty_schedule_preference.course.CourseRepository;
import ex.google.faculty_schedule_preference.user.UserRepository;

@RestController
public class RequestRestController {
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    RequestRestController(RequestRepository requestRepository, CourseRepository courseRepository,
            UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("api/requests/{request_id}")
    public ResponseEntity<?> View(@PathVariable long request_id) {
        return ResponseEntity.ok(requestRepository.findById(request_id).get().getTimes());
    }

    @PostMapping("api/requests/{request_id}")
    public ResponseEntity<?> submit_appriove(@RequestBody String approvedTime, @PathVariable long request_id,
            Errors errors) {
        System.out.println(approvedTime);
        if (errors.hasErrors()) {
            String result = errors.getAllErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body(result);

        }
        Request request = requestRepository.findById(request_id).get();
        request.setAproved_time(approvedTime);
        request.setStatus(2);
        requestRepository.save(request);
        return ResponseEntity.ok("okay");

    }

    @PostMapping("courses/{course_id}/request")
    public ResponseEntity<?> Add(@RequestBody String times, @PathVariable long course_id, Errors errors) {
        if (errors.hasErrors()) {
            String result = errors.getAllErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body(result);

        }

        Request newRequest = new Request(1, times, null, userRepository.findById(1l).get(),
                courseRepository.findById(course_id).get());
        requestRepository.save(newRequest);
        return ResponseEntity.ok("okay");
    }
}
