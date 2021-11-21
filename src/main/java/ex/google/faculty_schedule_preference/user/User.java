package ex.google.faculty_schedule_preference.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.request.Request;

@Entity(name = "User")
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "user_email_unique", columnNames = "email") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "csun_id", nullable = false, columnDefinition = "TEXT")
    private String csun_id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "username", nullable = false, columnDefinition = "TEXT")
    private String username;

    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests = new ArrayList<Request>();

    @OneToOne(fetch = FetchType.LAZY)

    private Department department;

    public User() {
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public User(String csun_id, String name, String username, String email, String password) {
        this.csun_id = csun_id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String csun_id, String name, String username, String email, String password, List<Request> requests) {
        this.csun_id = csun_id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.requests = requests;
    }

    public long getId() {
        return id;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
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
