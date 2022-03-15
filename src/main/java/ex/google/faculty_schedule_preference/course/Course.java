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
    @Transient
    static Map<Integer, String> weekDays = Map.of(1, "Sun", 2, "Mon", 3, "Tue", 4, "Wed", 5, "Thu", 6, "Fri", 7,
            "Sat");
    @Transient
    static Map<Integer, String> classType = Map.of(1, "Online", 2, "In-person", 3, "Hybrid");

    @Transient
    static Map<Integer, String> statusHuman = Map.of(1, "open", 2, "under review", 3, "closed");

    @Transient
    public static Map<String, Integer> statusValues = Map.of("open", 1, "under review", 2, "closed", 3);

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

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "weekSchedule", nullable = false)
    private String weekSchedule;

    @OneToOne(fetch = FetchType.LAZY)
    private Department department;

    @OneToOne(fetch = FetchType.LAZY)
    private Term term;

    public Course() {
    }

    public Course(String name, String prefix, double unit, int type, String daysOfWeek, String startTime,
            String endTime) {
        this.name = name;
        this.prefix = prefix;
        this.unit = unit;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Course(String name, String description, String prefix, double unit, int type, String daysOfWeek,
            String startTime,
            String endTime) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.unit = unit;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Course(String name, String description, String prefix, double unit, int type, String daysOfWeek,
            String startTime,
            String endTime, Term term) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.unit = unit;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(String weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public String getHumanClassType() {
        return classType.get(this.type);
    }

    public String getHumanStatus() {
        return statusHuman.get(this.type);
    }

}