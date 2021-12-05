package ex.google.faculty_schedule_preference.user_availability;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.google.faculty_schedule_preference.user_availability.UserAvailability;

public interface UserAvailabilityRepository extends JpaRepository<UserAvailability, Long> {

}