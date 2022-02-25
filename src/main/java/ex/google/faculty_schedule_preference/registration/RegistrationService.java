package ex.google.faculty_schedule_preference.registration;

import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import ex.google.faculty_schedule_preference.user.MyUserDetailsService;
import ex.google.faculty_schedule_preference.user.User;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private DepartmentRepository departmentRepository;

    public String register(RegistrationRequest request) {
        boolean isVaildEmail = emailValidator
                .test(request.getEmail());

        if (!isVaildEmail) {
            throw new IllegalStateException("email is not valid");
        }

        if(!(request.getPassword().equals(request.getConfirmPassword()))){
            throw new IllegalStateException("Passwords do not match");
        }

        System.out.println(request.getDepartment());

        return myUserDetailsService.signUpUser(
                new User(
                        request.getCsun_id(),
                        request.getName(),
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword(),
                        departmentRepository.findById(request.getDepartment()).get()));

    }
}
