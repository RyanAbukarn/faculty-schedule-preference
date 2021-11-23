package ex.google.faculty_schedule_preference.course;

import java.sql.Date;

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

@Entity(name = "Course")
@Table(name = "courses", uniqueConstraints = {
        @UniqueConstraint(name = "course_prefix_unique", columnNames = "prefix") })
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "course_name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(name = "prefix", nullable = false, columnDefinition = "TEXT")
    private String prefix;

    @Column(name = "unit", nullable = false)
    private double unit;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "start_date", nullable = false)
    private Date start_date;

    @Column(name = "end_date", nullable = false)
    private Date end_date;

    @OneToOne(fetch = FetchType.LAZY)
    private Department department;

    public Course() {
    }

    public Course(String name, String prefix, double unit, int type, Date start_date, Date end_date) {
        this.name = name;
        this.prefix = prefix;
        this.unit = unit;
        this.type = type;
        this.start_date = start_date;
        this.end_date = end_date;
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

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

}