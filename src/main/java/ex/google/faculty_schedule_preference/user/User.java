package ex.google.faculty_schedule_preference.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.document.Document;
import ex.google.faculty_schedule_preference.permission.Permission;
import ex.google.faculty_schedule_preference.request.Request;
import ex.google.faculty_schedule_preference.user_availability.UserAvailability;

@Entity(name = "User")
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(name = "user_email_unique", columnNames = "email") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "csun_id", nullable = false, columnDefinition = "TEXT")
    private String csunID;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "username", nullable = false, columnDefinition = "TEXT")
    private String username;

    @Column(name = "email", nullable = false, columnDefinition = "TEXT")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Column(name = "reset_password_token")
    private String resetPasswordToken = null;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Request> requests = new ArrayList<Request>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<Document>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_departments", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "department_id", referencedColumnName = "id"))
    private Set<Department> departments = new HashSet<>();;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAvailability> userAvailabilities;

    @Column(name = "entitlement", nullable = true)
    private double entitlement = 0.0;

    public User() {
    }

    public User(String csunID, String name, String username, String email, String password) {
        this.csunID = csunID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String csunID, String name, String username, String email, String password,
            Set<Department> departments) {
        this.csunID = csunID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.departments = departments;
    }

    public User(String csunID, String name, String username, String email, String password,
            Department department) {
        this.csunID = csunID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.departments.add(department);
    }

    public User(String csunID, String name, String username, String email, String password, Boolean enabled,
            Boolean locked) {
        this.csunID = csunID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.locked = locked;
    }

    public User(String csunID, String name, String username, String email, String password, List<Request> requests) {
        this.csunID = csunID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.requests = requests;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartment(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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

    public String getCsunID() {
        return csunID;
    }

    public void setCsunID(String csunID) {
        this.csunID = csunID;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public List<UserAvailability> getUserAvailabilities() {
        return userAvailabilities;
    }

    public void setUserAvailabilities(List<UserAvailability> userAvailabilities) {
        this.userAvailabilities = userAvailabilities;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public void pushBackPermissions(Permission permission) {
        this.permissions.add(permission);
    }

    public Document getResume() {
        if (!this.documents.isEmpty())
            return this.documents.stream().filter(doc -> doc.getType() == 1).findAny().get();
        return null;
    }

    public void setEntitlement(double entitlement) {
        this.entitlement = entitlement;
    }

    public double getEntitlement() {
        return this.entitlement;
    }

}
