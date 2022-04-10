package ex.google.faculty_schedule_preference.course;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import ex.google.faculty_schedule_preference.department.Department;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> getCoursesByDepartment(@Param("id") long id);

    List<Course> getCoursesByDepartment(Department department);

    List<Course> getCoursesByDepartmentIn(Set<Department> department);
}