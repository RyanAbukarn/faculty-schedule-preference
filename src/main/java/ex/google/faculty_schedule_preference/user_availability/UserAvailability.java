package ex.google.faculty_schedule_preference.user_availability;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ex.google.faculty_schedule_preference.release_time.ReleaseTime;
import ex.google.faculty_schedule_preference.term.Term;
import ex.google.faculty_schedule_preference.user.User;

@Entity(name = "UserAvailability")
@Table(name = "user_availabilities")
public class UserAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "times", nullable = false, columnDefinition = "TEXT")
    private String times;
    @Column(name = "max_unit", nullable = false)
    private double maxUnit;
    @Column(name = "min_unit", nullable = false)
    private double minUnit;
    

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Term term;

    @OneToMany(mappedBy = "userAvailability", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReleaseTime> releaseTimes = new ArrayList<ReleaseTime>();

    public UserAvailability() {
    }

    public UserAvailability(String times, double maxUnit, double minUnit) {
        this.times = times;
        this.maxUnit = maxUnit;
        this.minUnit = minUnit;
    }

    public UserAvailability(String times, double maxUnit, double minUnit, 
            User user) {
        this.times = times;
        this.maxUnit = maxUnit;
        this.minUnit = minUnit;
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public double getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnit(double maxUnit) {
        this.maxUnit = maxUnit;
    }

    public double getMinUnit() {
        return minUnit;
    }

    public void setMinUnit(double minUnit) {
        this.minUnit = minUnit;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public void setReleaseTimes(List<ReleaseTime> releaseTimes) {
        this.releaseTimes = releaseTimes;
    }

    public List<ReleaseTime> getReleaseTimes() {
        return releaseTimes;
    }
}
