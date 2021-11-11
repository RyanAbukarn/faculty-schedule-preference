package ex.google.faculty_schedule_preference.user_availability;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/{user_id}/user_availability")
public class UserAvailabilityController {
    private final UserAvailabilityRepository repository;

    UserAvailabilityController(UserAvailabilityRepository repository) {
        this.repository = repository;
    }
}
