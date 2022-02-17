package ex.google.faculty_schedule_preference.user;


import antlr.BaseAST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        //TODO: Send confirmation token

        return "it works";
    }

}