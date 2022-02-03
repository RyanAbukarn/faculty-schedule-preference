package ex.google.faculty_schedule_preference.user_availability;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.json.JSONArray;
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
import net.minidev.json.JSONObject;

@Controller
public class UserAvailabilityController {

  @Autowired
  private UserAvailabilityRepository repository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  DataSource dataSource;

  // Get: UserAvailability/Create 
  @GetMapping("user/{user_id}/user_availability")
  public String Index(@PathVariable("user_id") long user_id, Model model) throws SQLException{ 
      Connection con = dataSource.getConnection();
      String readQuery = "SELECT * FROM user_availabilities";
      PreparedStatement preparedStatement = con.prepareStatement(readQuery);
      ResultSet rs = preparedStatement.executeQuery();
      Long id = -1L;
      Long lastId = 0L;
      while(rs.next()){
            lastId = rs.getLong("id");
            if (rs.getLong("user_id") == user_id){
                  id = rs.getLong("id");
                  continue;
            }
      }

      // if user doesn't exist create availability row for them in DB
      if (id == -1L){
            readQuery = "INSERT INTO user_availabilities (id, times, max_unit, min_unit, release_time, user_id)";
            readQuery += "VALUES (?,?,0.0,0.0,0.0, ?)";
            preparedStatement = con.prepareStatement(readQuery);
            preparedStatement.setLong(1, lastId + 1);
            preparedStatement.setString(2, "[]");
            preparedStatement.setLong(3, user_id);
            preparedStatement.executeUpdate();
            id = lastId + 1;
      }

      con.close();
      preparedStatement.close();
      rs.close();

      model.addAttribute("user", userRepository.findById(user_id).get());
      model.addAttribute("user_availability", repository.findById(id).get());
      return "user_availability/create";
  }

  // Get: api/UserAvailability/Create
  @GetMapping("api/user/{user_id}/user_availability")
  public ResponseEntity<?> View(@PathVariable long user_id) throws SQLException {
      Connection con = dataSource.getConnection();
      String readQuery = "SELECT * FROM user_availabilities WHERE user_id = ?";
      PreparedStatement preparedStatement = con.prepareStatement(readQuery);
      preparedStatement.setLong(1, user_id);
      ResultSet rs = preparedStatement.executeQuery();
      String times = "[]";

      while(rs.next()){
            times = rs.getString("times");
      }   

      JSONArray jsonArray = new JSONArray(times);
      JSONObject responses = new JSONObject();
      responses.put("event", jsonArray.toString());

      con.close();
      preparedStatement.close();
      rs.close();
      return ResponseEntity.ok(responses.toString());
  }

  // Post: api/UserAvailability/Create
  @PostMapping("api/user/{user_id}/user_availability")
    public ResponseEntity<?> SubmitEvent(@RequestBody String dataFromView, @PathVariable long user_id,
    RedirectAttributes redirectAttributes) throws SQLException {
      // User user = userRepository.findById(user_id).get();
      // System.out.println(dataFromView);
        
        Connection con = dataSource.getConnection();
        String readQuery = "SELECT * FROM user_availabilities WHERE user_id = ?";
        PreparedStatement preparedStatement = con.prepareStatement(readQuery);
        preparedStatement.setLong(1, user_id);
        ResultSet rs = preparedStatement.executeQuery();
        Long id = -1L;

        while(rs.next()){
              id = rs.getLong("id");
        }

        String []data = dataFromView.split("]");
        String calendarTimes = data[0] + "]";

        String[] units = data[1].split(",");
        double min_unit = Double.parseDouble(units[1].substring(1,units[1].length()-1));
        double max_unit = Double.parseDouble(units[2].substring(1,units[2].length()-1));
        double release_time = Double.parseDouble(units[3].substring(1,units[3].length()-1));


        // update database
        String updateQuery = "UPDATE user_availabilities SET times = ?, max_unit = ?, min_unit = ?, release_time = ? WHERE id = ?";
        preparedStatement = con.prepareStatement(updateQuery);
        preparedStatement.setString(1, calendarTimes);
        preparedStatement.setDouble(2, max_unit);
        preparedStatement.setDouble(3, min_unit);
        preparedStatement.setDouble(4, release_time);
        preparedStatement.setLong(5, id);
        preparedStatement.executeUpdate();
        System.out.println("database updated calendar");

        con.close();
        preparedStatement.close();
        rs.close();
        redirectAttributes.addFlashAttribute("message", "Successfully updated availability and units");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return ResponseEntity.ok("okay");
    }
}
