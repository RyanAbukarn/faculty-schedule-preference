package ex.google.faculty_schedule_preference.department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Department")
@Table(name = "departments", uniqueConstraints = {
        @UniqueConstraint(name = "department_prefix_unique", columnNames = "prefix") })
public class Department {
    @Id
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
    @Column(name = "prefix", nullable = false, columnDefinition = "TEXT")
    private String prefix;

    public Department() {
    }

    public Department(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public Department(Long id, String name, String prefix) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

}