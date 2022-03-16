package ex.google.faculty_schedule_preference.token;

import ex.google.faculty_schedule_preference.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

public class ConfirmationToken {
    @SequenceGenerator(name = "confirmation_token_sequence", sequenceName = "confirmation_token_sequence", allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_token_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken() {
    }

    public ConfirmationToken(String token,
            LocalDateTime createdAt,
            LocalDateTime expiresAt,
            User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public LocalDateTime getConfirmedAt() {
        return this.confirmedAt;
    }

    public LocalDateTime getExpiresAt() {
        return this.expiresAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }
}
