package ex.google.faculty_schedule_preference.user_permission;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/{user_id}/user_availability")
public class UserPermissionController {
    private final UserPermissionRepository repository;

    UserPermissionController(UserPermissionRepository repository) {
        this.repository = repository;
    }
}
