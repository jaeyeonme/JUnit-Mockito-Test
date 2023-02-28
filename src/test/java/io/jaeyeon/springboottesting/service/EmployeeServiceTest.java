package io.jaeyeon.springboottesting.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.jaeyeon.springboottesting.exception.ResourceNotFoundException;
import io.jaeyeon.springboottesting.model.Employee;
import io.jaeyeon.springboottesting.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock private EmployeeRepository employeeRepository;
	@InjectMocks private EmployeeService employeeService;
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
	@DisplayName("JUint test for saveEmployee method")
	void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() throws Exception {
	    // given - precondition or setup
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
		given(employeeRepository.save(employee)).willReturn(employee);

		System.out.println(employeeRepository);
		System.out.println(employeeService);

		// when - action or the behaviour that we are going test
		Employee savedEmployee = employeeService.saveEmployee(employee);

		System.out.println(savedEmployee);
		// then - verify the output
		assertThat(savedEmployee).isNotNull();
	}

	@Test
	@DisplayName("JUint test for saveEmployee method which throws exception")
	void givenExistingEmail_whenSaveEmployee_thenThrowsException() throws Exception {
		// given - precondition or setup
		given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

		System.out.println(employeeRepository);

		// when - action or the behaviour that we are going test
		assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.saveEmployee(employee);
		});
		System.out.println(employeeService);

		// then - verify the output
		verify(employeeRepository, never()).save(any(Employee.class));
	}

	@Test
	@DisplayName("JUnit test for getAllEmployees method")
	void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		// given - precondition or setup
		Employee employee1 = Employee.builder()
					.firstName("Brin")
					.lastName("Sergey")
					.email("cjyeon1022@google.com")
					.build();


		given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

		// when - action or the behaviour that we are going test
		List<Employee> employeeList = employeeRepository.findAll();

		// then - verify the output
		assertThat(employeeList).isNotNull();
		assertThat(employeeList.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("JUnit test for getAllEmployees method (negative scenario)")
	void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() throws Exception {
		// given - precondition or setup
		Employee employee1 = Employee.builder()
			.firstName("Brin")
			.lastName("Sergey")
			.email("cjyeon1022@google.com")
			.build();


		given(employeeRepository.findAll()).willReturn(Collections.emptyList());

		// when - action or the behaviour that we are going test
		List<Employee> employeeList = employeeRepository.findAll();

		// then - verify the output
		assertThat(employeeList).isEmpty();
		assertThat(employeeList.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("JUnit test for getEmployeeById method")
	void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup
		given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

		// when - action or the behaviour that we are going test
		Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

		// then - verify the output
		assertThat(savedEmployee).isNotNull();
	}

	@Test
	@DisplayName("JUnit test for updateEmployee method")
	void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
		// given - precondition or setup
		/**
		 * -> given(employeeRepository.save(employee)).willReturn(employee); <-
		 *
		 * Typically, stubbing argument mismatch indicates user mistake when writing tests.
		 * Mockito fails early so that you can debug potential problem easily.
		 * However, there are legit scenarios when this exception generates false negative signal:
		 *   - stubbing the same method multiple times using 'given().will()' or 'when().then()' API
		 *     Please use 'will().given()' or 'doReturn().when()' API for stubbing.
		 *   - stubbed method is intentionally invoked with different arguments by code under test
		 *     Please use default or 'silent' JUnit Rule (equivalent of Strictness.LENIENT).
		 * For more information see javadoc for PotentialStubbingProblem class.
		 *
		 * 이 에러 메시지에서는 strict stubbing argument mismatch 문제가 발생했다는 것을 알려주고 있다.
		 * 이 문제를 해결하기 위해서는 given().will() 대신 will().given() 또는 doReturn().when() API를 사용해야 한다.
		 *
		 * 그리고 해당 에러 메시지에서는 stubbed method is intentionally invoked with different arguments by code under test
		 * 라는 메시지도 함께 보여주고 있다.
		 * 이 메시지는 테스트 대상 코드에서 의도적으로 다른 매개변수를 사용하여 스텁된 메서드를 호출한 것으로 보인다.
		 *
		 * 따라서, updateEmployee() 메서드에서 updatedEmployee 매개변수를 사용하여 employeeRepository.save() 메서드를 호출하므로,
		 * 해당 매개변수를 사용하여 스텁을 설정해야 한다.
		 *
		 * 따라서, 다음과 같이 employeeRepository.save() 메서드에 대한 스텁을 설정하면 된다.
		 */
		// any(): Employee 클래스와 일치하는 어떤 객체도 매개변수로 받아들일 수 있도록 한다.
		// willAnswer(): 주어진 Answer를 사용하여 메서드를 호출할 때 결과를 반환하도록 설정한다.
		// invocation.getArgument(0): save() 메소드의 첫 번째 매개변수를 반환하는 코드.
		// updateEmployee() 메서드에서 employeeRepository.save() 메서드를 호출할 때 전달된 Employee 객체가 반환되도록 설정.
		given(employeeRepository.save(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

		Employee updatedEmployee = Employee.builder()
			.firstName("Tim")
			.lastName("Cook")
			.email("Tim@apple.com")
			.build();

		// when - action or the behaviour that we are going test
		Employee result = employeeService.updateEmployee(updatedEmployee);

		// then - verify the output
		assertThat(result.getEmail()).isEqualTo("Tim@apple.com");
		assertThat(result.getFirstName()).isEqualTo("Tim");
	}

	@Test
	@DisplayName("JUint test for deleteEmployee method")
	void givenEmployeeId_whenDeleteEmployee_thenNothing() throws Exception {
		// given - precondition or setup
		long employeeId = 1L;

		willDoNothing().given(employeeRepository).deleteById(employeeId);

		// when - action or the behaviour that we are going test
		employeeService.deleteEmployee(employeeId);

		// then - verify the output
		verify(employeeRepository, times(1)).deleteById(employeeId);
	}
}
