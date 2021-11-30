package ex.google.faculty_schedule_preference;

import ex.google.faculty_schedule_preference.department.Department;
import ex.google.faculty_schedule_preference.department.DepartmentRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

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
			HashMap<String, String> departments = new HashMap<String, String>();

			// Add departments by key-value pairs
			departments.put("ENGL","English");
			departments.put("HIST","History");
			departments.put("HUM", "Humanities");
			departments.put("AFRS", "Africana Studies");
			departments.put("BIO", "Biology");
			departments.put("CHEM", "Chemistry");
			departments.put("PHYS", "Physics");
			departments.put("KINS", "Kinesiology");
			departments.put("MATH", "Math");
			departments.put("COMP", "Computer Science");
			departments.put("ME", "Mechanical Engineering");
			departments.put("ECE", "Electrical Engineering");

			// Check if Repo is full
			// If repo entries are less than 12, populate the repo
			// Else, return message.
			if(repo.count() < 12) {
				System.out.println("Populating repo...");
				departments.forEach((abrv, dpt) -> {
					boolean found = false;
					for(Department x: repo.findAll()) {
						if(x.getPrefix().equalsIgnoreCase(abrv) && x.getName().equalsIgnoreCase(dpt)){
							found = true;
						}
					}
					if(!found)
						repo.save(new Department(dpt, abrv));
				});
			}
			else {
				System.out.println("Database has previously been populated.");
			}
		};

	}

}
