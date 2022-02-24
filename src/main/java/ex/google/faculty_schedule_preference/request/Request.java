package ex.google.faculty_schedule_preference.request;

import ex.google.faculty_schedule_preference.course.Course;
import ex.google.faculty_schedule_preference.request_feedback.Requestfeedback;
import ex.google.faculty_schedule_preference.term.Term;
import ex.google.faculty_schedule_preference.user.User;

import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name = "request")
@Table(name = "requests")
public class Request {

    @Transient
    public static Map<String, Integer> statusValues = Map.of("new", 1, "under_review", 2, "denied", 3, "accpeted",
            4);
    @Transient

    private Map<Integer, String> humanStatusMap = Map.of(1, "New", 2, "Under review", 3, "Denied", 4, "Accpeted");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "status", nullable = false)
    private int status;

    @JsonProperty("times")
    @Column(name = "times", nullable = false, columnDefinition = "TEXT")
    private String times;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    private Course course;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requestfeedback> requestFeedbacks = new ArrayList<Requestfeedback>();

    @OneToOne(fetch = FetchType.LAZY)
    private Term term;

    public Request() {
    }

    public Request(int status, String times, User user, Course course) {
        this.status = status;
        this.times = times;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String humanStatus() {
        return humanStatusMap.get(this.status);
    }

    public List<Requestfeedback> getRequestFeedbacks() {
        return requestFeedbacks;
    }

    public void setRequestFeedbacks(List<Requestfeedback> requestFeedbacks) {
        this.requestFeedbacks = requestFeedbacks;
    }

    public void pushRequestFeedback(Requestfeedback requestFeedback) {
        this.requestFeedbacks.add(requestFeedback);
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

}
