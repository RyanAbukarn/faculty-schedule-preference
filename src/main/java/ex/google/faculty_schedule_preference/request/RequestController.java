package ex.google.faculty_schedule_preference.request;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/{user_id}/request")
public class RequestController {
    private final RequestRepository repository;

    RequestController(RequestRepository repository) {
        this.repository = repository;
    }
}
