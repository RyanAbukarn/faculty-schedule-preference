package ex.google.faculty_schedule_preference.user;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private UserValidator emailValidator;
    @Autowired
    private DepartmentRepository departmentRepository;

    public String register(UserInput request) {
        boolean isVaildEmail = emailValidator
                .test(request.getEmail());

        if (!isVaildEmail) {
            throw new IllegalStateException("email is not valid");
        }

        if (!(request.getPassword().equals(request.getConfirmPassword()))) {
            throw new IllegalStateException("Passwords do not match");
        }

        // I assume only one department is added at the signup page...
        Department department = departmentRepository.findById(request.getDepartment()).get();

        return myUserDetailsService.signUpUser(
                new User(
                        request.getCsun_id(),
                        request.getName(),
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword(),
                        department));
    }
}
