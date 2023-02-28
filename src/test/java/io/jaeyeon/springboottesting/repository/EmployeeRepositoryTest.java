package io.jaeyeon.springboottesting.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.jaeyeon.springboottesting.model.Employee;

/**
 * Spring Boot provides the @DataJpaTest annotation to test the persistence layer components that will autoconfigure
 * in-memory embedded database for testing purposes.
 */
@DataJpaTest
class EmployeeRepositoryTest {

	@Autowired private EmployeeRepository employeeRepository;

	private Employee employee;

	@BeforeEach
	void setup() {
		employee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();
	}

	@Test
	@DisplayName("JUnit test for save employee operation")
	void givenEmployeeObject_whenSave_thenReturnSavedEmployee() throws Exception {
		// when - action or the behaviour that we are going test
		Employee savedEmployee = employeeRepository.save(employee);

		// then - verify the output
		Assertions.assertThat(savedEmployee).isNotNull();
		Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
	}

	@Test
	@DisplayName("JUnit test for get all employees operation")
	void givenEmployeeList_whenFindAll_thenEmployeeList() throws Exception {
	    // given - precondition or setup
		Employee employee1 = Employee.builder()
			.firstName("Brin")
			.lastName("Sergey")
			.email("cjyeon1022@google.com")
			.build();

		employeeRepository.save(employee);
		employeeRepository.save(employee1);

		// when - action or the behaviour that we are going test
		List<Employee> employeeList = employeeRepository.findAll();

		// then - verify the output
		Assertions.assertThat(employeeList).isNotNull();
		Assertions.assertThat(employeeList.size()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("JUnit test for get employee by id operation")
	void givenEmployeeObject_whenFindByID_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup
		employeeRepository.save(employee);

		// when - action or the behaviour that we are going test
		Employee employeeDB = employeeRepository.findById(employee.getId()).get();

		// then - verify the output
		Assertions.assertThat(employeeDB).isNotNull();
	}

	@Test
	@DisplayName("JUnit test for get employee by email operation")
	void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup
		employeeRepository.save(employee);

		// when - action or the behaviour that we are going test
		Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

		// then - verify the output
		Assertions.assertThat(employeeDB).isNotNull();
	}

	@Test
	@DisplayName("JUnit test for update employee operation")
	void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
		// given - precondition or setup
		employeeRepository.save(employee);

		// when - action or the behaviour that we are going test
		String newEmail = "cjyeon1022@google.com";
		String firstName = "Test";
		Employee newEmployee = Employee.builder()
			.firstName(firstName)
			.lastName("Cho")
			.email(newEmail)
			.build();
		Employee updatedEmployee = employeeRepository.save(newEmployee);

		// then - verify the output
		Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("cjyeon1022@google.com");
		Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Test");

	}

	@Test
	@DisplayName("JUnit test for delete employee operation")
	void givenEmployeeObject_whenDelete_thenRemoveEmployee() throws Exception {
		// given - precondition or setup
		employeeRepository.save(employee);

		// when - action or the behaviour that we are going test
		employeeRepository.deleteById(employee.getId());
		Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

		// then - verify the output
		Assertions.assertThat(employeeOptional).isEmpty();
	}

	@Test
	@DisplayName("JUnit test for custom query using JPQL with index")
	void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup
		employeeRepository.save(employee);
		String firstName = "Jaeyeon";
		String lastName = "Cho";

		// when - action or the behaviour that we are going test
		Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

		// then - verify the output
		Assertions.assertThat(savedEmployee).isNotNull();
	}
}
