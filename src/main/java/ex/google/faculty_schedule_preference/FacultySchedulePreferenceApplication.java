package ex.google.faculty_schedule_preference;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class FacultySchedulePreferenceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacultySchedulePreferenceApplication.class, args);
	}
	@Bean
	public ApplicationRunner DDBInitializer(DepartmentRepository repo){
		return (args) -> {
			System.out.println("running"); //debug
			// Create departments HashMap
			HashMap<Long, String> departments = new HashMap<Long, String>();

			// Add departments by key-value pairs
			departments.put(1L, "ENGL:English");
			departments.put(2L, "HIST:History");
			departments.put(3L, "HUM:Humanities");
			departments.put(4L, "AFRS:Africana Studies");
			departments.put(5L, "BIO:Biology");
			departments.put(6L, "CHEM:Chemistry");
			departments.put(7L, "PHYS:Physics");
			departments.put(8L, "KINS:Kinesiology");
			departments.put(9L, "MATH:Math");
			departments.put(10L, "COMP:Computer Science");
			departments.put(11L, "ME:Mechanical Engineering");
			departments.put(12L, "ECE:Electrical Engineering");
			departments.put(13L, "PSY:Psychology");

			// Check if Repo is full
			// If repo entries are greater than 0, delete and populate the repo
			System.out.println("Populating repo...");
			departments.forEach((id, dpt) -> {
				String[] splitted = dpt.split(":");
				if(!repo.existsById(id))
					repo.save(new Department(id, splitted[1], splitted[0]));
			});

		};

	}

}
