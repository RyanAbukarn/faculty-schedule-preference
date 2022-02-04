package ex.google.faculty_schedule_preference.user_availability;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAvailabilityRepository extends JpaRepository<UserAvailability, Long> {
}