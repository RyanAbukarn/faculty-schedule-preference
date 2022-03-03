package ex.google.faculty_schedule_preference.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.department.id = :id")
    List<Course> getCoursesByDepartment(@Param("id") long id);
}