package ex.google.faculty_schedule_preference.registration;

import com.sun.xml.bind.v2.TODO;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {
    @Override
    public boolean test(String s) {
      //  TODO: Regex to validate email
        return true;
    }
}
