package ex.google.faculty_schedule_preference.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.authorizeRequests()
                .antMatchers("/courses/{course_id}/request")
                .hasAnyRole("ADMIN", "CONTROLLER", "TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/departments").hasAnyRole("ADMIN", "CONTROLLER", "TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/*").hasAnyRole("ADMIN", "CONTROLLER", "TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/requests/**").hasAnyRole("CONTROLLER", "SUPERUSER")
                .antMatchers("/my-requests/**").hasAnyRole("TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/courses/{course_id}/request").hasAnyRole("TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/users/{user_id}/permissions").hasAnyRole("ADMIN", "SUPERUSER", "CONTROLLER")
                .antMatchers("/users/{user_id}/user_availability")
                .hasAnyRole("ADMIN", "CONTROLLER", "TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/users/upload-resume")
                .hasAnyRole("ADMIN", "CONTROLLER", "TENURETRACK", "LECTURER", "SUPERUSER")
                .antMatchers("/users")
                .hasAnyRole("ADMIN", "CONTROLLER", "SUPERUSER")
                .antMatchers("/").permitAll()
                .and().formLogin()
                .loginPage("/users/login")
                .loginProcessingUrl("/users/login")
                .defaultSuccessUrl("/")
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/src/main/resources/static/css/**", "/src/main/resources/static/images/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}