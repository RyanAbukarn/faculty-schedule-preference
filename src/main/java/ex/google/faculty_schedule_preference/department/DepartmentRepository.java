package ex.google.faculty_schedule_preference.department;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByPrefix(String prefix);

    Department findByName(String departmentName);
}