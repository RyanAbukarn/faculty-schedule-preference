package ex.google.faculty_schedule_preference.request;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ex.google.faculty_schedule_preference.course.CourseRepository;
import ex.google.faculty_schedule_preference.user.User;
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

    @GetMapping("api/requests/{request_id}/approved")
    public ResponseEntity<?> Approved(@PathVariable long request_id) {
        Request theRequest = requestRepository.findById(request_id).get();
        return ResponseEntity.ok(theRequest.getAproved_time());

    }

    @GetMapping("api/requests/{request_id}")
    public ResponseEntity<?> View(@PathVariable long request_id) {
        Request theRequest = requestRepository.findById(request_id).get();
        theRequest.setStatus(2);
        requestRepository.save(theRequest);
        return ResponseEntity.ok(theRequest.getTimes());

    }

    @PostMapping("api/requests/{request_id}")
    public ResponseEntity<?> SubmitAprroveTime(@RequestBody String approvedTime, @PathVariable long request_id) {
        Request request = requestRepository.findById(request_id).get();
        request.setAproved_time(approvedTime);
        request.setStatus(3);
        requestRepository.save(request);
        return ResponseEntity.ok("okay");

    }

    @PostMapping("courses/{course_id}/request")
    public ResponseEntity<?> Add(@RequestBody String times, @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable long course_id) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
        Request newRequest = new Request(1, times, null,
                currentUser,
                courseRepository.findById(course_id).get());
        requestRepository.save(newRequest);
        return ResponseEntity.ok("okay");
    }
}
