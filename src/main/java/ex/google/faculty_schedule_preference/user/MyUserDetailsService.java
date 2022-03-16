package ex.google.faculty_schedule_preference.user;

import ex.google.faculty_schedule_preference.permission.Permission;
import ex.google.faculty_schedule_preference.permission.PermissionRepository;

import ex.google.faculty_schedule_preference.user.token.ConfirmationToken;
import ex.google.faculty_schedule_preference.user.token.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        if (!user.get().getEnabled()) {
            throw new UsernameNotFoundException("User is disabled");
        }

        return user.map(MyUserDetails::new).get();
    }

    public String signUpUser(User user) {
        boolean emailExists = userRepository.existsByEmail(user.getEmail());

        boolean usernameExists = userRepository.existsByUsername(user.getUsername());

        if (emailExists) {
            throw new IllegalStateException("Email Already taken");
        }

        if (usernameExists) {
            throw new IllegalStateException("Username is already in use");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        Permission lecturePermission = permissionRepository.getById(4L);
        user.pushBackPermissions(lecturePermission);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

}