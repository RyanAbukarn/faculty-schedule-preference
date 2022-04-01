package ex.google.faculty_schedule_preference.course;

import javax.persistence.Transient;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.term.Term;

@Entity(name = "Course")
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(name = "course_prefix_unique", columnNames = "prefix") })
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "prefix", nullable = false, columnDefinition = "TEXT")
    private String prefix;

    @Column(name = "unit", nullable = false)
    private double unit;

    @Column(name = "status", nullable = false)
    private int status;

    @OneToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    private Term term;

    public Course() {
    }

    public Course(String name, String prefix, double unit) {
        this.name = name;
        this.prefix = prefix;
        this.unit = unit;
    }

    public Course(String name, String description, String prefix, double unit) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.unit = unit;
    }

    public Course(String name, String description, String prefix, double unit, Term term) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.unit = unit;
        this.term = term;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

}