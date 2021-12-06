package ex.google.faculty_schedule_preference.request_feedback;

import ex.google.faculty_schedule_preference.request.Request;
import ex.google.faculty_schedule_preference.user.User;
import javax.persistence.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Map;

@Entity(name = "Requestfeedback")
@Table(name = "request_feedbacks")
public class Requestfeedback {
    @Transient
    private static Map<Integer, String> recivers = Map.of(1, "Requester", 2, "Staff");

    public static String getViewByKey(Integer key) {
        return recivers.get(key);
    }

    public static Map<Integer, String> getReciverTyps() {
        return recivers;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "reciver", nullable = true)
    private Integer reciver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Requestfeedback() {
    }

    public Requestfeedback(String comment) {
        this.comment = comment;
    }

    public Requestfeedback(String comment, Request request) {
        this.comment = comment;
        this.request = request;
    }

    public Integer getReciver() {
        return reciver;
    }

    public void setView(Integer reciver) {
        this.reciver = reciver;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
