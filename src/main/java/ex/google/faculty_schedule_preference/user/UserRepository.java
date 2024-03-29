package ex.google.faculty_schedule_preference.user;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.google.faculty_schedule_preference.department.Department;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Set<User> findAllByDepartmentsIn(Set<Department> departments);

    boolean existsByEmail(String string);

    boolean existsByUsername(String username);

    User getByUsername(String username);

    User getByEmail(String email);

    public User findByResetPasswordToken(String token);
}
