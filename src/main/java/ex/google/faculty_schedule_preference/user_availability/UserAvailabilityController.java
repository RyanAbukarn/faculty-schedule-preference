package ex.google.faculty_schedule_preference.user_availability;

import org.json.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ex.google.faculty_schedule_preference.user.User;
import ex.google.faculty_schedule_preference.user.UserRepository;

@Controller
public class UserAvailabilityController {

      @Autowired
      private UserAvailabilityRepository repository;

      @Autowired
      private UserRepository userRepository;

      @Autowired
      DataSource dataSource;

      @GetMapping("user/{user_id}/user_availability")
      public String Index(@PathVariable("user_id") long user_id, Model model) {
            User user = userRepository.findById(user_id).get();

            UserAvailability userAvailabilities = user.getUserAvailabilities();
            if (userAvailabilities.equals(null))
                  userAvailabilities = new UserAvailability();

            model.addAttribute("user", user);
            model.addAttribute("user_availability", userAvailabilities);
            return "user_availability/create";
      }

      // Get: api/UserAvailability/Create
      @GetMapping("api/user/{user_id}/user_availability")
      public ResponseEntity<?> View(@PathVariable long user_id) {
            User user = userRepository.findById(user_id).get();
            UserAvailability userAvailabilities = user.getUserAvailabilities();

            JSONArray jsonArray = new JSONArray(userAvailabilities.getBody());
            JSONObject responses = new JSONObject();
            responses.put("event", jsonArray.toString());

            return ResponseEntity.ok(responses.toString());
      }

      // Post: api/UserAvailability/Create
      @PostMapping("api/user/{user_id}/user_availability")
      public ResponseEntity<?> SubmitEvent(@RequestBody String dataFromView, @PathVariable long user_id,
                  RedirectAttributes redirectAttributes) {
            User user = userRepository.findById(user_id).get();
            JSONObject newJSONObject = new JSONObject(dataFromView);
            JSONObject newUnit = newJSONObject.getJSONObject("units");
            UserAvailability userAvailabilities = user.getUserAvailabilities();
            if (userAvailabilities.equals(null))
                  userAvailabilities = new UserAvailability();

            userAvailabilities.setBody(newJSONObject.optJSONArray("dates").toString());
            userAvailabilities.setMax_unit(Double.parseDouble(newUnit.getString("max_unit")));
            userAvailabilities.setMin_unit(Double.parseDouble(newUnit.getString("min_unit")));
            userAvailabilities.setRelease_time(Double.parseDouble(newUnit.getString("release_time")));
            userAvailabilities.setUser(user);
            repository.save(userAvailabilities);

            redirectAttributes.addFlashAttribute("message", "Successfully updated availability and units");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return ResponseEntity.ok("okay");
      }
}
