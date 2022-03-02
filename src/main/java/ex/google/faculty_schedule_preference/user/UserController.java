package ex.google.faculty_schedule_preference.user;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import ex.google.faculty_schedule_preference.document.Document;
import ex.google.faculty_schedule_preference.document.DocumentRepository;
import ex.google.faculty_schedule_preference.permission.Permission;
import ex.google.faculty_schedule_preference.permission.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.box.sdk.BoxAPIConnection;

import com.box.sdk.BoxFolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.HashSet;

@Controller

@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Value("${boxapi}")
    private String boxapi;

    // function is called to load updateRoles page
    // http://localhost:3001/users/1/permissions
    @RequestMapping(value = "/{user_id}/permissions", method = RequestMethod.GET)
    String getRoles(@PathVariable("user_id") long user_id, Model model) {
        User user = repository.findById(user_id).get();
        model.addAttribute("user", user);

        HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
        permissions.put("ROLE_ADMIN", false);
        permissions.put("ROLE_CONTROLLER", false);
        permissions.put("ROLE_TENURETRACK", false);
        permissions.put("ROLE_LECTURER", false);
        for (Permission x : user.getPermissions()) {
            permissions.put(x.getRole(), true);
        }
        model.addAttribute("permissions", permissions);
        return "permission/updateRoles";
    }

    // after admin clicks submit, function is called
    @RequestMapping(value = "/{user_id}/permissions", method = RequestMethod.POST)
    public String postRoles(@PathVariable("user_id") long user_id,
            @RequestParam("permissions") List<Long> permissions,
            RedirectAttributes redirectAttributes) {
        // remove everything
        // re-add what is checked
        User user = repository.findById(user_id).get();
        user.getPermissions().clear();
        Set<Permission> permissionsSet = new HashSet<>(permissionRepository.findAllById(permissions));
        user.setPermissions(permissionsSet);
        repository.save(user);
        redirectAttributes.addFlashAttribute("message", "Success");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/" + user_id + "/permissions";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "user/login";
        }
        return "redirect:../";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/users/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("user", new User());
        return "user/signup";
    }

    @PostMapping("/upload-resume")
    public String postUploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException, NoSuchAlgorithmException {
        BoxAPIConnection api = new BoxAPIConnection(boxapi);
        BoxFolder parentFolder = new BoxFolder(api, "0");

        User currentUser = repository.findByUsername(userDetails.getUsername()).get();
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        String sha3Hex = md.digest(file.getOriginalFilename().getBytes(StandardCharsets.UTF_8)).toString();
        String fileName = currentUser.getCsun_id() + " - " + sha3Hex;

        BoxFolder.Info childFolderInfo = parentFolder.createFolder(fileName);
        Document resume = currentUser.getResume();
        if (currentUser.getResume() != null) {
            BoxFolder folder = new BoxFolder(api, currentUser.getResume().getname());
            folder.delete(true);
            resume.setname(childFolderInfo.getID());
        } else {
            resume = new Document(Document.typeValues.get("RESUME"), childFolderInfo.getID(),
                    currentUser);
        }
        documentRepository.save(resume);

        BoxFolder rootFolder = childFolderInfo.getResource();
        rootFolder.uploadFile(file.getInputStream(), file.getOriginalFilename());

        redirectAttributes.addFlashAttribute("message", "Successfully uploaded the resume");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/upload-resume";
    }

    @GetMapping("/upload-resume")
    public String uploadFile() {
        return "user/upload_resume";
    }

    @GetMapping("/{user_id}/departments")
    public String getDepartments(@PathVariable("user_id") long user_id, Model model) {
        // get the requested user_id
        User user = repository.findById(user_id).get();
        model.addAttribute("user", user);

        List<Department> departmentsList = departmentRepository.findAll();

        // pre-fill departments to be true if the user is in department, otherwise put
        // it as false
        HashMap<String, Boolean> departments = new HashMap<String, Boolean>();
        for (Department d : departmentsList) {
            departments.put(d.getName(), false);
        }
        for (Department userDepartment : user.getDepartments()) {
            departments.put(userDepartment.getName(), true);
        }
        model.addAttribute("departments", departments);

        return "department/updateDepartments";
    }

    @PostMapping("/{user_id}/departments")
    public String postDepartments(@PathVariable("user_id") long user_id,
            @RequestParam("departments") List<String> departments,
            RedirectAttributes redirectAttributes) {
        User user = repository.findById(user_id).get();
        for (String department : departments) {
            System.out.println(department);
        }
        // I have the department names but I want to get the Departments
        Set<Department> departmentsSet = new HashSet<Department>();
        for (String department : departments) {
            departmentsSet.add(departmentRepository.findByName(department));
        }
        user.getDepartments().clear();
        user.setDepartment(departmentsSet);
        repository.save(user);
        redirectAttributes.addFlashAttribute("message", "Success");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/" + user_id + "/departments";
    }

    // Function sends User data to View where Controller, Admin or Superuser have
    // access to.
    @GetMapping("")
    public String manageUsers(Model model, HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        List<User> users;
        users = repository.findAll();
        // User currentUser =
        // repository.findByUsername(userDetails.getUsername()).get();
        // make it so that I can't edit permissions of users that are not in my
        // department as a controller
        // if the current user is an admin or superuser send all users to View
        // if (request.isUserInRole("ROLE_ADMIN") ||
        // request.isUserInRole("ROLE_SUPERUSER")) {
        // users = repository.findAll();
        // } else {
        // if the current user is a controller then only send users within the same
        // department
        // users = repository.findAllByDepartmentsIn(currentUser.getDepartments());
        // }

        model.addAttribute("users", users);

        return "user/index";
    }

}
