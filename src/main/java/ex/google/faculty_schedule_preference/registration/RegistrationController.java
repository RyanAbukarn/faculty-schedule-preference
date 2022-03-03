package ex.google.faculty_schedule_preference.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users/registration")
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("signup")
    public String register(RegistrationRequest request) {
        return registrationService.register(request);
    }
}
