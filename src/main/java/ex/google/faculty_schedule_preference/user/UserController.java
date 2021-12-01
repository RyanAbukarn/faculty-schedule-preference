package ex.google.faculty_schedule_preference.user;

import ex.google.faculty_schedule_preference.permission.Permission;
import ex.google.faculty_schedule_preference.permission.PermissionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller

@RequestMapping("user")
public class UserController {
    private final UserRepository repository;
    private final PermissionRepository permissionRepository;

    UserController(UserRepository repository, PermissionRepository permissionRepository) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
    }

    // function is called to load updateRoles page
    // http://localhost:3001/user/1/permissions
    @RequestMapping(value = "/{user_id}/permissions", method = RequestMethod.GET)
    String getRoles(@PathVariable("user_id") long user_id, Model model)
    {
        User user = repository.findById(user_id).get();
        model.addAttribute("user", user);

        HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
        permissions.put("ROLE_ADMIN",false);
        permissions.put("ROLE_CONTROLLER",false);
        permissions.put("ROLE_TENURETRACK", false);
        permissions.put("ROLE_LECTURER", false);
        for (Permission x: user.getPermissions()) {
            permissions.put(x.getRole(), true);
        }
        model.addAttribute("permissions", permissions);
        return "permission/updateRoles";
    }

    // after admin clicks submit, function is called
    @RequestMapping(value = "/{user_id}/permissions", method = RequestMethod.POST)
    public String postRoles(@PathVariable("user_id") long user_id, @RequestParam("permissions1") List<Long> permissions1,
                            RedirectAttributes redirectAttributes)
    {
        // remove everything
        // re-add what is checked
        List<Permission> permissions = new ArrayList<Permission>();
        User user = repository.findById(user_id).get();
        user.getPermissions().clear();
        user.setPermissions(permissionRepository.findAllById(permissions1));
        repository.save(user);
        redirectAttributes.addFlashAttribute("message", "Success");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/user/"+user_id+"/permissions";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

}
