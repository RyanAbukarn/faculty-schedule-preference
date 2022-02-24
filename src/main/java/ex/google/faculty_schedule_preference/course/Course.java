package ex.google.faculty_schedule_preference.course;

import javax.persistence.Transient;
import java.sql.Time;
import java.util.Map;

import javax.persistence.CascadeType;
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
//    @Transient
//    private Map<Integer, String> weekDays = Map.of(1, "Sun", 2, "Mon", 3, "Tue", 4, "Wed", 5, "Thu", 6, "Fri", 7,
//            "Sat");
//    @Transient
//    private Map<Integer, String> classType = Map.of(1, "Online", 2, "In-person", 3, "Hybrid");
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

    @Column(name = "start_time", nullable = true)
    private String startTime;

    @Column(name = "end_time", nullable = true)
    private String endTime;

//    @OneToOne(fetch = FetchType.LAZY)
//    private Department department;
    
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Department department;
	

    public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

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

	@Override
	public String toString() {
		return "Course [id=" + id + ", name=" + name + ", prefix=" + prefix + ", unit=" + unit + ", type=" + type
				+ ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
    
    

}