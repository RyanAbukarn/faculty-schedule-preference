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

@Entity(name = "RequestFeedback")
@Table(name = "request_feedbacks")
public class RequestFeedback {
    @Transient
    private static Map<Integer, String> receivers = Map.of(1, "Requester", 2, "Staff");

    public static String getViewByKey(Integer key) {
        return receivers.get(key);
    }

    public static Map<Integer, String> getReceiverTypes() {
        return receivers;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "receiver", nullable = true)
    private Integer receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Request request;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public RequestFeedback() {
    }

    public RequestFeedback(String comment) {
        this.comment = comment;
    }

    public RequestFeedback(String comment, Request request, Integer receiver) {
        this.comment = comment;
        this.request = request;
        this.receiver = receiver;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
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

    public String getHumanReceiver() {
        return receivers.get(this.receiver);
    }
}
