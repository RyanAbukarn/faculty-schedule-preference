package ex.google.faculty_schedule_preference.request;

import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class RequestRestController {
    @Autowired
    private RequestRepository requestRepository;

    @PostMapping("api/requests/{request_id}")
    public ResponseEntity<?> SubmitAprroveTime(@RequestBody String approvedTime, @PathVariable long request_id,
            RedirectAttributes redirectAttributes) {
        Request request = requestRepository.findById(request_id).get();
        JSONObject newJSONObject = new JSONObject(approvedTime);
        JSONArray jsonArray = new JSONArray(request.getTimes());

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).getString("title").equals(newJSONObject.getString("title")))
                jsonArray.getJSONObject(i).put("color", "#AFE1AF");
        }
        request.setTimes(jsonArray.toString());
        request.setStatus(Request.statusValues.get("accpeted"));
        requestRepository.save(request);
        redirectAttributes.addFlashAttribute("message", "Successfully Approved The Time");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return ResponseEntity.ok("okay");

    }

}
