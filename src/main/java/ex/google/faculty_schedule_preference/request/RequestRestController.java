package ex.google.faculty_schedule_preference.request;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("api/requests/{request_id}")
    public ResponseEntity<?> View(@PathVariable long request_id) {

        Request theRequest = requestRepository.findById(request_id).get();
        JSONArray jsonArray = new JSONArray(theRequest.getTimes());
        JSONObject responces = new JSONObject();
        responces.put("event", jsonArray.toString());
        responces.put("status", theRequest.getStatus());

        if (theRequest.getStatus() == Request.statusValues.get("new"))
            theRequest.setStatus(Request.statusValues.get("under_review"));

        return ResponseEntity.ok(responces.toString());
    }

    @PostMapping("api/requests/{request_id}")
    public ResponseEntity<?> SubmitAprroveTime(@RequestBody String approvedTime, @PathVariable long request_id,
            RedirectAttributes redirectAttributes) {
        Request request = requestRepository.findById(request_id).get();
        JSONObject newJSONObject = new JSONObject(approvedTime);
        JSONArray jsonArray = new JSONArray(request.getTimes());

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("title").equals(newJSONObject.getString("title")))
                jsonArray.getJSONObject(i).put("color", "#AFE1AF");
        }
        request.setTimes(jsonArray.toString());
        request.setStatus(Request.statusValues.get("accpeted"));
        requestRepository.save(request);
        redirectAttributes.addFlashAttribute("message", "Successfully Approved The Time");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return ResponseEntity.ok("okay");

    }

    @PostMapping("courses/{course_id}/request")
    public ResponseEntity<?> Add(@RequestBody String times, @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable long course_id, RedirectAttributes redirectAttributes) {

        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();

        Request newRequest = new Request(1, times,
                currentUser,
                courseRepository.findById(course_id).get());
        requestRepository.save(newRequest);
        return ResponseEntity.ok("okay");
    }
}
