package ex.google.faculty_schedule_preference.user;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.google.faculty_schedule_preference.department.Department;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Set<User> findAllByDepartmentsIn(Set<Department> departments);

}