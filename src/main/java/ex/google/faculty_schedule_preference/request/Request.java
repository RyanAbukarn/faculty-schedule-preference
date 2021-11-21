package ex.google.faculty_schedule_preference.request;

import ex.google.faculty_schedule_preference.course.Course;
import ex.google.faculty_schedule_preference.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "request")
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "status", nullable = false)
    private int status;
    @Column(name = "priority", nullable = false)
    private int priority;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    private Course course;

    public Request() {
    }

}
