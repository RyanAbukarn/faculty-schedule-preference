package ex.google.faculty_schedule_preference.registration;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "user/registration")
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("signup")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }
}
