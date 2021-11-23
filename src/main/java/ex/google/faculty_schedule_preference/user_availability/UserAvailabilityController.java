package ex.google.faculty_schedule_preference.user_availability;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("user/{user_id}/uavailability")
public class UserAvailabilityController {
    private final UserAvailabilityRepository repository;

    UserAvailabilityController(UserAvailabilityRepository repository) {
        this.repository = repository;
    }
}
