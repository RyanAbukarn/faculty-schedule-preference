package ex.google.faculty_schedule_preference.user_availability;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
    @Column(name = "release_time", nullable = false)
    private double releaseTime;
    @Column(name = "source_description", nullable = false)
    private String sourceDescription;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    private Term term;

    public UserAvailability() {
    }

    public UserAvailability(String times, double maxUnit, double minUnit, double releaseTime,
            String sourceDescription) {
        this.times = times;
        this.maxUnit = maxUnit;
        this.minUnit = minUnit;
        this.releaseTime = releaseTime;
        this.sourceDescription = sourceDescription;
    }

    public UserAvailability(String times, double maxUnit, double minUnit, double releaseTime,
            String sourceDescription, User user) {
        this.times = times;
        this.maxUnit = maxUnit;
        this.minUnit = minUnit;
        this.releaseTime = releaseTime;
        this.sourceDescription = sourceDescription;
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

    public double getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(double releaseTime) {
        this.releaseTime = releaseTime;
    }

    public void setSourceDescription(String sourceDescription) {
        this.sourceDescription = sourceDescription;
    }

    public String getSourceDescription() {
        return sourceDescription;
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

}
