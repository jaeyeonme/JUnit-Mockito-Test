package io.jaeyeon.springboottesting.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import io.jaeyeon.springboottesting.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);

	// define custom query using JPQL with named params
	@Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
	Employee findByJPQL(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
