package ex.google.faculty_schedule_preference.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ex.google.faculty_schedule_preference.course.Course;
import ex.google.faculty_schedule_preference.user.User;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> getAllByUser(User currentUser);

    List<Request> getAllByCourseIn(List<Course> courses);
}