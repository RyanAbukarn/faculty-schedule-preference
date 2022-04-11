package ex.google.faculty_schedule_preference.release_time;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ReleaseTimeRepository extends JpaRepository<ReleaseTime, Long>{

    List<ReleaseTime> getAllByUserAvailabilityId(long id);

}

