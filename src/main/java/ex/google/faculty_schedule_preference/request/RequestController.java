package ex.google.faculty_schedule_preference.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.stereotype.Controller;

import ex.google.faculty_schedule_preference.user.User;

@Controller

public class RequestController {
    @Autowired
    private RequestRepository repository;

    @GetMapping("requests")
    public String Index(Model model) {
        model.addAttribute("request", "");
        return "request";
    }

    @GetMapping("requests/{request_id}")
    public String View(@PathVariable("request_id") long request_id, Model model) {
        model.addAttribute("request", "");
        return "request";
    }

    @PostMapping("requests/{request_id}")
    public String Edit(@PathVariable("request_id") long request_id, Model model) {
        model.addAttribute("request", "");
        return "request";
    }

    @GetMapping("courses/{course_id}/request")
    public String Create(@PathVariable("course_id") long course_id, Model model) {
        model.addAttribute("request", new Request());
        return "/request/create";
    }

    @PostMapping("courses/{course_id}/request")
    public String Add(@PathVariable("course_id") long course_id, Model model) {
        model.addAttribute("request", course_id);
        return "request";
    }

}
