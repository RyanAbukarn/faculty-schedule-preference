package ex.google.faculty_schedule_preference.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class RegistrationRequest {
    private final String csun_id;
    private final String name;
    private final String username;
    private final String email;
    private final String password;
}
