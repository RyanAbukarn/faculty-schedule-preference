package ex.google.faculty_schedule_preference.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        http.authorizeRequests()
                .antMatchers("upload_resume/**",
                        "/my_availabilities/**",
                        "/my_requests/**",
                        "/courses/{course_id}/request/**",
                        "/courses",
                        "/users/logout")
                .authenticated()
                .antMatchers("/requests/**").hasAnyRole("CONTROLLER", "SUPERUSER")
                .antMatchers(
                        "/courses/{course_id}/**",
                        "/users/",
                        "/users",
                        "/users/{user_id}/permissions/**",
                        "/users/{user_id}/user_availability/**",
                        "/users/{user_id}/entitlements/",
                        "/terms/**")
                .hasAnyRole("ADMIN", "CONTROLLER", "SUPERUSER")
                .antMatchers(
                        "/users/{token}/confirm", 
                        "/users/signup",
                        "/users/forgotPassword",
                        "/users/resetPassword",
                        "/users/login_validation"
                ).permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/users/login")
                .loginProcessingUrl("/users/login_validation")
                .failureForwardUrl("/users/login_validation")
                .defaultSuccessUrl("/")
                .permitAll().and().logout().logoutUrl("/users/logout")
                .logoutSuccessUrl("/users/login")
                .invalidateHttpSession(true);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/images/**");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}