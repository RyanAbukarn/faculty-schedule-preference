package ex.google.faculty_schedule_preference.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxSharedLink.Access;
import com.box.sdk.sharedlink.BoxSharedLinkRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ex.google.faculty_schedule_preference.course.Course;
import ex.google.faculty_schedule_preference.course.CourseRepository;
import ex.google.faculty_schedule_preference.document.Document;
import ex.google.faculty_schedule_preference.request_feedback.RequestFeedback;
import ex.google.faculty_schedule_preference.user.User;
import ex.google.faculty_schedule_preference.user.UserRepository;
import ex.google.faculty_schedule_preference.user_availability.UserAvailability;
import ex.google.faculty_schedule_preference.user_availability.UserAvailabilityRepository;

@Controller
public class RequestController {
    @Value("${boxapi}")
    private String boxapi;
    @Autowired
    private RequestRepository repository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAvailabilityRepository userAvailabilityRepository;

    @GetMapping("requests")
    public String index(Model model) {
        model.addAttribute("requests", repository.findAll());
        return "request/index";
    }

    @GetMapping("my_requests")
    public String myRequests(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
        List<Request> theRequests = currentUser.getRequests();
        Map<Long, List<RequestFeedback>> requestFeedbacks = new HashMap<Long, List<RequestFeedback>>();
        theRequests.stream().forEach(x -> requestFeedbacks.put(x.getId(),
                x.getRequestFeedbacks().stream().filter(y -> y.getReceiver() == 1).collect(Collectors.toList())));
        model.addAttribute("requests", theRequests);
        model.addAttribute("requestFeedbacks", requestFeedbacks);
        return "request/my_requests";
    }

    @PostMapping("requests/{request_id}")
    public String update(@PathVariable("request_id") long request_id,
            RequestFeedback requestFeedback, Model model,
            RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        Request theRequest = repository.findById(request_id).get();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
        requestFeedback.setRequest(theRequest);
        requestFeedback.setUser(currentUser);
        theRequest.pushRequestFeedback(requestFeedback);
        repository.save(theRequest);
        redirectAttributes.addFlashAttribute("message", "Successfully added a comment");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/requests/" + request_id;
    }

    @PostMapping("requests/{request_id}/approved-time")
    public String submitApproveTime(@RequestParam("potentialApprovedTime") String potentialApprovedTime,
            @RequestParam("userAvailability") String userAvailability,
            @PathVariable("request_id") long request_id,
            RedirectAttributes redirectAttributes) {
        Request request = repository.findById(request_id).get();
        request.setStatus(Request.statusValues.get("accepted"));
        request.setApprovedTime(potentialApprovedTime);
        List<UserAvailability> userAvailabilities = request.getUser().getUserAvailabilities();
        UserAvailability currentUserAvailability = userAvailabilities.get(userAvailabilities.size() - 1);
        currentUserAvailability.setTimes(userAvailability);
        userAvailabilityRepository.save(currentUserAvailability);
        repository.save(request);
        redirectAttributes.addFlashAttribute("message", "Successfully Approved The Times");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/requests";
    }

    @GetMapping("requests/{request_id}")
    public String view(@PathVariable("request_id") long request_id, Model model) {
        BoxAPIConnection api = new BoxAPIConnection(boxapi);
        BoxSharedLinkRequest sharedLinkRequest = new BoxSharedLinkRequest().access(Access.OPEN).permissions(true, true);
        Request theRequest = repository.findById(request_id).get();
        Document doc = theRequest.getUser().getResume();
        String url = "";
        if (doc != null) {
            BoxFolder folder = new BoxFolder(api, doc.getName());
            url = folder.createSharedLink(sharedLinkRequest).getURL();
        }

        model.addAttribute("request", theRequest);
        model.addAttribute("user", theRequest.getUser());
        model.addAttribute("course", theRequest.getCourse());
        model.addAttribute("request_feedback", new RequestFeedback());
        model.addAttribute("request_feedbacks", theRequest.getRequestFeedbacks());
        model.addAttribute("receiver_types", RequestFeedback.getReceiverTypes());
        model.addAttribute("resume", url);
        return "request/view";
    }

    @PostMapping("requests/{request_id}/denied")
    public String denied(@PathVariable("request_id") long request_id,
            RedirectAttributes redirectAttributes) {
        Request theRequest = repository.findById(request_id).get();
        theRequest.setStatus(Request.statusValues.get("denied"));
        repository.save(theRequest);
        redirectAttributes.addFlashAttribute("message", "Successfully Denied The Request");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/requests/" + request_id;
    }

    @PostMapping("course_requests")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes,
            @RequestParam(value = "myArray[]") Long[] array) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
        List<UserAvailability> userAvailabilities = currentUser.getUserAvailabilities();
        int count = 1;

        Course course = null;
        Request request = null;
        if (userAvailabilities.isEmpty()) {
            return ResponseEntity.ok("my_availabilities");
        }
        for (Long i : array) {
            course = courseRepository.findById(i).get();
            request = new Request();
            request.setCourse(course);
            request.setStatus(Request.statusValues.get("new"));
            request.setTimes(userAvailabilities.get(userAvailabilities.size() -
                    1).getTimes());
            request.setUser(currentUser);
            request.setPreference(count);
            count++;
            repository.save(request);
        }

        return ResponseEntity.ok("my_requests");
    }

}
