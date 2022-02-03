package ex.google.faculty_schedule_preference.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ex.google.faculty_schedule_preference.course.CourseRepository;
import ex.google.faculty_schedule_preference.request_feedback.Requestfeedback;
import ex.google.faculty_schedule_preference.user.User;
import ex.google.faculty_schedule_preference.user.UserRepository;

import org.springframework.stereotype.Controller;

@Controller
public class RequestController {
    @Autowired
    private RequestRepository repository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("requests")
    public String Index(Model model) {
        model.addAttribute("requests", repository.findAll());
        return "request/index";
    }

    @GetMapping("my-requests")
    public String MyRquests(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
        List<Request> theRequests = currentUser.getRequests();
        Map<Long, List<Requestfeedback>> requestFeedbacks = new HashMap<Long, List<Requestfeedback>>();
        theRequests.stream().forEach(x -> requestFeedbacks.put(x.getId(),
                x.getRequestFeedbacks().stream().filter(y -> y.getReciver() == 1).collect(Collectors.toList())));
        model.addAttribute("requests", theRequests);
        model.addAttribute("requestFeedbacks", requestFeedbacks);
        return "request/my_requests";
    }

    @PostMapping("requests/{request_id}")
    public String Add(@PathVariable("request_id") long request_id,
            Requestfeedback requestfeed, Model model,
            RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        Request theRequest = repository.findById(request_id).get();
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
        requestfeed.setRequest(theRequest);
        requestfeed.setUser(currentUser);
        theRequest.pushRequestFeedback(requestfeed);
        repository.save(theRequest);
        redirectAttributes.addFlashAttribute("message", "Successfully added a comment");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/requests/" + request_id;
    }

    @GetMapping("requests/{request_id}")
    public String View(@PathVariable("request_id") long request_id, Model model) {
        Request theRequest = repository.findById(request_id).get();
        model.addAttribute("request", theRequest);
        model.addAttribute("user", theRequest.getUser());
        model.addAttribute("course", theRequest.getCourse());
        model.addAttribute("request_feedback", new Requestfeedback());
        model.addAttribute("request_feedbacks", theRequest.getRequestFeedbacks());
        model.addAttribute("reciverTypes", Requestfeedback.getReciverTyps());
        return "request/view";
    }

    @PostMapping("requests/{request_id}/denied")
    public String Denied(@PathVariable("request_id") long request_id,
            RedirectAttributes redirectAttributes) {
        Request theRequest = repository.findById(request_id).get();
        theRequest.setStatus(Request.statusValues.get("denied"));
        repository.save(theRequest);
        redirectAttributes.addFlashAttribute("message", "Successfully Denied The Reqeust");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/requests/" + request_id;
    }

    @GetMapping("courses/{course_id}/request")
    public String Create(@PathVariable("course_id") long course_id, Model model) {
        model.addAttribute("request", new Request());
        model.addAttribute("course", courseRepository.findById(course_id).get());
        return "request/create";
    }

}
