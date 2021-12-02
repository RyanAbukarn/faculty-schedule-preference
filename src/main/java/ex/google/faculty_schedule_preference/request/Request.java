package ex.google.faculty_schedule_preference.request;

import ex.google.faculty_schedule_preference.course.Course;
import ex.google.faculty_schedule_preference.user.User;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity(name = "request")
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "status", nullable = false)
    private int status;

    @JsonProperty("times")
    @Column(name = "times", nullable = false)
    private String times;
    @JsonProperty("aproved_time")

    @Column(name = "aproved_time", nullable = true)
    private String aproved_time;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    private Course course;

    public Request() {
    }

    public Request(int status, String times, String aproved_time, User user, Course course) {
        this.status = status;
        this.times = times;
        this.aproved_time = aproved_time;
        this.user = user;
        this.course = course;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getAproved_time() {
        return aproved_time;
    }

    public void setAproved_time(String aproved_time) {
        this.aproved_time = aproved_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
