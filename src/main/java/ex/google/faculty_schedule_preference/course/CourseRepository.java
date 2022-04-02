package ex.google.faculty_schedule_preference.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> getCoursesByDepartment(@Param("id") long id);
}