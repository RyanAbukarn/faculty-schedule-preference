package ex.google.faculty_schedule_preference.user_availability;

import ex.google.faculty_schedule_preference.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "UserAvailability")
@Table(name = "user_availabilities")
public class UserAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "body", nullable = false)
    private String body;
    @Column(name = "max_unit", nullable = false)
    private double max_unit;
    @Column(name = "min_unit", nullable = false)
    private double min_unit;
    @Column(name = "release_time", nullable = false)
    private double release_time;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public UserAvailability(String body, double max_unit, double min_unit, double release_time) {
        this.body = body;
        this.max_unit = max_unit;
        this.min_unit = min_unit;
        this.release_time = release_time;
    }

    public UserAvailability(String body, double max_unit, double min_unit, double release_time, User user) {
        this.body = body;
        this.max_unit = max_unit;
        this.min_unit = min_unit;
        this.release_time = release_time;
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getMax_unit() {
        return max_unit;
    }

    public void setMax_unit(double max_unit) {
        this.max_unit = max_unit;
    }

    public double getMin_unit() {
        return min_unit;
    }

    public void setMin_unit(double min_unit) {
        this.min_unit = min_unit;
    }

    public double getRelease_time() {
        return release_time;
    }

    public void setRelease_time(double release_time) {
        this.release_time = release_time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
