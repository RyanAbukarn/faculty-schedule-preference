package ex.google.faculty_schedule_preference.user;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class UserValidator implements Predicate<String> {
  @Override
  public boolean test(String s) {
    // TODO: Regex to validate email
    return true;
  }
}
