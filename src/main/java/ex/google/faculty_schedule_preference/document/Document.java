package ex.google.faculty_schedule_preference.document;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ex.google.faculty_schedule_preference.user.User;

@Entity(name = "Document")
@Table(name = "documents")
public class Document {
    @Transient
    public static Map<String, Integer> typeValues = Map.of("RESUME", 1);
    @Transient
    private static Map<Integer, String> humanTypeValues = Map.of(1, "RESUME");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Document() {
    }

    public Document(String name) {
        this.name = name;

    }

    public Document(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Document(int type, String name, User user) {
        this.type = type;
        this.name = name;
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
