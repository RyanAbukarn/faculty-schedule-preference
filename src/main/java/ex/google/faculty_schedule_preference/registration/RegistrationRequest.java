package ex.google.faculty_schedule_preference.registration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegistrationRequest {
    private String csun_id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private long department;

    public long getDepartment() {
        return department;
    }

    public void setDepartment(long department) {
        this.department = department;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getCsun_id() {
        return csun_id;
    }

    public void setCsun_id(String csun_id) {
        this.csun_id = csun_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
