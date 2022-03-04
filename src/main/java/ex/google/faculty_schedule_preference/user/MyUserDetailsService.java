package ex.google.faculty_schedule_preference.user;


import antlr.BaseAST;
import ex.google.faculty_schedule_preference.permission.Permission;
import ex.google.faculty_schedule_preference.permission.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return user.map(MyUserDetails::new).get();
    }

    public String signUpUser(User user){
        boolean emailExists = userRepository.findByUsername(user.getEmail()).isPresent();

        boolean usernameExists = userRepository.findByUsername(user.getUsername()).isPresent();

        if (emailExists){
            throw new IllegalStateException("Email Already taken");
        }

        if (usernameExists){
            throw new IllegalStateException("Username is already in use");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        //System.out.println(user.getPassword());

        user.setPassword(encodedPassword);
        Permission lecturePermission = permissionRepository.getById(4L);
        System.out.println(lecturePermission.getRole());
        Set<Permission> permissions = new HashSet<>(); // by default a new user will have the role of lecturer
        permissions.add(lecturePermission);
        user.setPermissions(permissions);

        userRepository.save(user);
        

        //TODO: Send confirmation token

        return "it works";
    }

}