package ex.google.faculty_schedule_preference.release_time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ex.google.faculty_schedule_preference.user_availability.UserAvailability;

@Entity(name = "ReleaseTime")
@Table(name = "release_times")
public class ReleaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;
    @Column(name = "units", nullable = false, scale = 2)
    private double units;
    @Column(name = "source", nullable = false)
    private String source;
    @Column(name = "note", nullable = false)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAvailability userAvailability;

    public ReleaseTime(){}

    public ReleaseTime(double units, String source, String note, UserAvailability userAvailability){
        this.units = units;
        this.source = source;
        this.note = note;
        this.userAvailability = userAvailability;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getUnits() {
        return units;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setUserAvailability(UserAvailability userAvailability) {
        this.userAvailability = userAvailability;
    }

    public UserAvailability getUserAvailability() {
        return userAvailability;
    }
}
