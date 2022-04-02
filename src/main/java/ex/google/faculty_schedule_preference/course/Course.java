package ex.google.faculty_schedule_preference.course;

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
        @UniqueConstraint(name = "course_number_unique", columnNames = "number") })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    // Example: 490, 491L
    @Column(name = "number", nullable = false, columnDefinition = "TEXT")
    private String number;

    @Column(name = "unit", nullable = false)
    private double unit;

    @Column(name = "k_factor", nullable = true)
    private String k_factor;

    @Column(name = "enrollmentBased", nullable = false)
    private Boolean enrollmentBased;

    @OneToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    private Term term;

    public Course() {
    }

    public Course(String name, String number, double unit) {
        this.name = name;
        this.number = number;
        this.unit = unit;
    }

    public Course(String name, String description, String number, double unit) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.unit = unit;
    }

    public Course(String name, String description, String number, double unit,
                    String k_factor, Boolean enrollmentBased,  Department department, Term term) {
        this.name = name;
        this.description = description;
        this.number = number;
        this.unit = unit;
        this.k_factor = k_factor;
        this.enrollmentBased = enrollmentBased;
        this.department = department;
        this.term = term;        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }
    
    public String getK_factor(){
        return k_factor;
    }

    public void setK_factor(String k_factor){
        this.k_factor = k_factor;
    }

    public Boolean getEnrollmentBased(){
        return enrollmentBased;
    }

    public void setEnrollmentBased(Boolean enrollmentBased){
        this.enrollmentBased = enrollmentBased;
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