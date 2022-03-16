package ex.google.faculty_schedule_preference.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.resource.spi.IllegalStateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import ex.google.faculty_schedule_preference.document.Document;
import ex.google.faculty_schedule_preference.document.DocumentRepository;
import ex.google.faculty_schedule_preference.email.EmailSender;
import ex.google.faculty_schedule_preference.permission.Permission;
import ex.google.faculty_schedule_preference.permission.PermissionRepository;

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

    @Autowired
    private UserService userService;

    @Autowired
    private EmailSender emailSender;

    @Value("${boxapi}")
    private String boxapi;

    // function is called to load updateRoles page
    // http://localhost:3001/users/1/permissions
    @RequestMapping(value = "/{user_id}/permissions", method = RequestMethod.GET)
    String getRoles(@PathVariable("user_id") long user_id, Model model, HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User user = repository.findById(user_id).get();
        User currentUser = repository.findByUsername(userDetails.getUsername()).get();
        boolean isAdminOrControllerOrSuperuser = request.isUserInRole("ROLE_SUPERUSER")
                || request.isUserInRole("ROLE_ADMIN");
        boolean anyMatchInDepartment = currentUser.getDepartments().stream()
                .anyMatch(department -> user.getDepartments().contains(department));
        if (anyMatchInDepartment || isAdminOrControllerOrSuperuser) {
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
        redirectAttributes.addFlashAttribute("message", "Can't access because you don't have permissions");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        return "redirect:/users";
    }

    // after admin clicks submit, function is called
    @RequestMapping(value = "/{user_id}/permissions", method = RequestMethod.POST)
    public String postRoles(@PathVariable("user_id") long user_id, HttpServletRequest request,
            @RequestParam("permissions") List<Long> permissions, @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        // remove everything
        // re-add what is checked

        User currentUser = repository.findByUsername(userDetails.getUsername()).get();
        User user = repository.findById(user_id).get();
        boolean isAdminOrControllerOrSuperuser = request.isUserInRole("ROLE_SUPERUSER")
                || request.isUserInRole("ROLE_ADMIN");
        boolean anyMatchInDepartment = currentUser.getDepartments().stream()
                .anyMatch(department -> user.getDepartments().contains(department));
        if (anyMatchInDepartment || isAdminOrControllerOrSuperuser) {
            user.getPermissions().clear();
            Set<Permission> permissionsSet = new HashSet<>(permissionRepository.findAllById(permissions));
            user.setPermissions(permissionsSet);
            repository.save(user);
            redirectAttributes.addFlashAttribute("message", "Success");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/users/" + user_id + "/permissions";
        }
        redirectAttributes.addFlashAttribute("message", "Can't access because you don't have permissions");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        return "redirect:/users";

    }

    @GetMapping("/login")
    public String login(RedirectAttributes redirectAttributes, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("error_enabled", false);
            return "user/login";
        }
        return "redirect:../..";
    }

    @PostMapping("/login_validation")
    public String loginValidation(RedirectAttributes redirectAttributes, @RequestParam("username") String username,
            Model model) {
        User user = repository.getByUsername(username);
        if (!user.getEnabled()) {
            model.addAttribute("error_enabled", true);
            return "user/login";
        }
        return "redirect:../..";
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

    @PostMapping("/signup")
    public String register(UserInput request)
            throws IllegalStateException, UnsupportedEncodingException, MessagingException {
        userService.register(request);
        return "user/email-activation";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "user/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = UUID.randomUUID().toString();

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = userService.getSiteURL(request) + "/users/resetPassword/" + token;
            emailSender.sendReset(email, userService.buildResetEmail(email, resetPasswordLink));
            model.addAttribute("message", "We have sent a reset password link, please check your email.");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }

        return "user/forgotPassword";
    }

    @GetMapping("/resetPassword/{token}")
    public String showResetPasswordForm(@PathVariable(value = "token") String tokenURL,
            Model model) {
        User user = userService.getByResetPasswordToken(tokenURL);
        model.addAttribute("token", tokenURL);

        if (user == null) {
            model.addAttribute("message", "InvalidToken");
            return "message";
        }

        return "user/resetPassword";
    }

    @PostMapping("/resetPassword/{token}")
    public String processResetPassword(@PathVariable(value = "token") String tokenURL, HttpServletRequest request,
            Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("message", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
        } else {
            userService.updatePassword(user, password);

            model.addAttribute("message", "Your password has successfully been reset");
        }

        return "message";
    }

    @PostMapping("/upload_resume")
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

        redirectAttributes.addFlashAttribute("message");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/upload_resume";
    }

    @GetMapping("/upload_resume")
    public String uploadFile() {
        return "user/upload_resume";
    }

    @GetMapping("/{user_id}/departments")
    public String getDepartments(@PathVariable("user_id") long user_id, Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = repository.findById(user_id).get();

        model.addAttribute("user", user);
        List<Department> departments = departmentRepository.findAll();
        HashMap<String, Boolean> userDepartments = new HashMap<String, Boolean>();
        for (Department userDepartment : user.getDepartments()) {
            userDepartments.put(userDepartment.getName(), true);
        }
        model.addAttribute("userDepartments", userDepartments);
        model.addAttribute("departments", departments);
        return "user/edit_user_departments";

    }

    @PostMapping("/{user_id}/departments")
    public String postDepartments(@PathVariable("user_id") long user_id,
            @RequestParam("departments") List<Long> departments,
            RedirectAttributes redirectAttributes) {
        User user = repository.findById(user_id).get();
        user.getDepartments().clear();
        Set<Department> departmentSet = new HashSet<>(departmentRepository.findAllById(departments));
        user.setDepartment(departmentSet);
        repository.save(user);
        redirectAttributes.addFlashAttribute("message", "Success");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/users/" + user_id + "/departments";
    }

    @GetMapping("/{user_id}/entitlements")
    public String getEntitlements(@PathVariable("user_id") long user_id, Model model) {
        User user = repository.findById(user_id).get();
        model.addAttribute("user", user);
        return "user/edit_user_entitlements";
    }

    @PostMapping("/{user_id}/entitlements")
    public String postEntitlements(@PathVariable("user_id") long user_id,
            RedirectAttributes redirectAttributes, @RequestParam("entitlement") String entitlement) {
        double tempEntitlement = Double.parseDouble(entitlement);
        User user = repository.findById(user_id).get();
        user.setEntitlement(tempEntitlement);
        repository.save(user);
        return "redirect:/users/" + user_id + "/entitlements";
    }

    // Function sends User data to View where Controller, Admin or SuperUser have
    // access to.
    @GetMapping("")
    public String manageUsers(Model model, HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = repository.findByUsername(userDetails.getUsername()).get();

        HashMap<Long, Boolean> users = new HashMap<Long, Boolean>();

        List<User> usersList = repository.findAll();

        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_SUPERUSER")) {
            for (User u : usersList) {
                users.put(u.getId(), true);
            }
        }

        else {
            // if the current User is a controller then only allow them to edit users within
            // same department(s)
            for (User u : usersList) {
                users.put(u.getId(), false);
                for (Department d : currentUser.getDepartments()) {
                    if (u.getDepartments().contains(d)) {
                        users.put(u.getId(), true);
                    }
                }
            }
        }
        model.addAttribute("users", usersList);
        model.addAttribute("usersPermission", users);
        return "user/index";
    }

    @GetMapping("/{token}/confirm")
    public String confirm(@PathVariable("token") String token) throws IllegalStateException {

        userService.confirmToken(token);
        return "user/email-activated";
    }
}
