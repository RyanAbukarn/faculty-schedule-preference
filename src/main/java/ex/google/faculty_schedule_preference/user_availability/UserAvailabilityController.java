package ex.google.faculty_schedule_preference.user_availability;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ex.google.faculty_schedule_preference.term.TermRepository;
import ex.google.faculty_schedule_preference.user.User;
import ex.google.faculty_schedule_preference.user.UserRepository;

@Controller
public class UserAvailabilityController {

      @Autowired
      private UserAvailabilityRepository repository;

      @Autowired
      private UserRepository userRepository;
      @Autowired
      private TermRepository termRepository;

      @Autowired
      DataSource dataSource;

      @GetMapping("user_availabilities")
      public String Index(Model model) {
            model.addAttribute("userAvailabilities", repository.findAll());
            return "user_availability/index";
      }

      @GetMapping("users/{user_id}/user_availability")
      public String userAvailabilities(@PathVariable("user_id") long user_id, Model model) {
            User user = userRepository.findById(user_id).get();
            model.addAttribute("userAvailabilities", user.getUserAvailabilities());
            return "user_availability/user_availabilities";
      }

      @GetMapping("users/{user_id}/user_availability/{user_availability_id}")
      public String Index(@PathVariable("user_id") long user_id,
                  @PathVariable("user_availability_id") long user_availability_id, Model model) {
            model.addAttribute("user", userRepository.findById(user_id).get());
            model.addAttribute("user_availability", repository.findById(user_availability_id).get());
            return "user_availability/view";
      }

      @GetMapping("users/{user_id}/user_availability/new")
      public String new_(@PathVariable("user_id") long user_id, Model model) {
            User user = userRepository.findById(user_id).get();
            model.addAttribute("terms", termRepository.findAll());
            model.addAttribute("user", user);
            model.addAttribute("user_availability", new UserAvailability());
            return "user_availability/new";
      }

      // Post: api/UserAvailability/Create
      @PostMapping("users/{user_id}/user_availability/create")
      public String SubmitEvent(@ModelAttribute UserAvailability userAvailability,
                  @PathVariable long user_id,
                  RedirectAttributes redirectAttributes) {
            User user = userRepository.findById(user_id).get();
            userAvailability.setTimes(userAvailability.getTimes());
            userAvailability.setUser(user);
            repository.save(userAvailability);
            redirectAttributes.addFlashAttribute("message", "Successfully updated availability and units");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect: user_availability/index";
      }
}
