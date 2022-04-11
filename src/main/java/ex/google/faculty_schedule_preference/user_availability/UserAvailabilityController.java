package ex.google.faculty_schedule_preference.user_availability;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ex.google.faculty_schedule_preference.release_time.ReleaseTime;
import ex.google.faculty_schedule_preference.release_time.ReleaseTimeRepository;
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
      private ReleaseTimeRepository releaseTimeRepository;

      @GetMapping("user_availabilities")
      public String index(Model model) {
            model.addAttribute("userAvailabilities", repository.findAll());
            return "user_availability/index";
      }

      @GetMapping("users/{user_id}/user_availability")
      public String userAvailabilities(@PathVariable("user_id") long user_id, Model model) {
            User user = userRepository.findById(user_id).get();
            model.addAttribute("userAvailabilities", user.getUserAvailabilities());
            model.addAttribute("title", user.getName() + "'s");

            return "user_availability/user_availabilities";
      }

      @GetMapping("users/{user_id}/user_availability/{user_availability_id}")
      public String view(@PathVariable("user_id") long user_id,
                  @PathVariable("user_availability_id") long user_availability_id, Model model) {
            model.addAttribute("user", userRepository.findById(user_id).get());
            model.addAttribute("user_availability", repository.findById(user_availability_id).get());
            return "user_availability/view";
      }

      @GetMapping("my_availabilities")
      public String myAvailabilities(Model model, @AuthenticationPrincipal UserDetails userDetails,
                  @RequestParam(required = false, value = "error") Boolean error) {
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
            model.addAttribute("userAvailabilities", currentUser.getUserAvailabilities());
            model.addAttribute("title", "My");
            if (error != null && error) {
                  model.addAttribute("message",
                              " In order to request to teach a course, your availability needs to be created first.");
                  model.addAttribute("alertClass", "alert-warning");
            }

            return "user_availability/user_availabilities";
      }

      @GetMapping("my_availabilities/{id}")
      public String viewMyAvailability(Model model, @PathVariable("id") long id,
                  @AuthenticationPrincipal UserDetails userDetails) {
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
            model.addAttribute("user", currentUser);
            model.addAttribute("user_availability", repository.findById(id).get());
            model.addAttribute("release_times", releaseTimeRepository.getAllByUserAvailabilityId(id));
            return "user_availability/view";
      }

      @GetMapping("my_availability/new")
      public String myAvailabilityNew(Model model, @AuthenticationPrincipal UserDetails userDetails) {
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
            model.addAttribute("terms", termRepository.findAll());
            model.addAttribute("user", currentUser);
            model.addAttribute("user_availability", new UserAvailability());
            return "user_availability/new";
      }

      // Post: api/UserAvailability/Create
      @PostMapping("my_availability/create")
      public String myAvailabilityCreate(@ModelAttribute UserAvailability userAvailability,
                  @AuthenticationPrincipal UserDetails userDetails, @RequestParam("releaseTimeArray") String [] releaseTimeArray,
                  RedirectAttributes redirectAttributes) {
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();
            
            userAvailability.setUser(currentUser);
            repository.save(userAvailability);

            for (int i = 0; i < releaseTimeArray.length; i +=3){
                  ReleaseTime releaseTime = new ReleaseTime();
                  releaseTime.setUnits(Double.parseDouble(releaseTimeArray[i]));
                  releaseTime.setSource(releaseTimeArray[i+1]);
                  releaseTime.setNote(releaseTimeArray[i+2]);
                  releaseTime.setUserAvailability(userAvailability);
                  releaseTimeRepository.save(releaseTime);
            }

            redirectAttributes.addFlashAttribute("message", "Successfully updated availability and units");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:../my_availabilities";
      }
}
