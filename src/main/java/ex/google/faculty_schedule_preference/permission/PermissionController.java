package ex.google.faculty_schedule_preference.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

@RequestMapping("user/{user_id}/permission")
public class PermissionController {
    private final PermissionRepository repository;
    PermissionController(PermissionRepository repository) {
        this.repository = repository;
    }
}