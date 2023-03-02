package io.jaeyeon.springboottesting.integration;

import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jaeyeon.springboottesting.model.Employee;
import io.jaeyeon.springboottesting.repository.EmployeeRepository;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerITest {

	// MockMvc 클래스를 사용하여 다른 HTTP 요청을 수행
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		employeeRepository.deleteAll();
	}

	@Test
	@DisplayName("JUnit test for Post employees REST API")
	void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
		// given - precondition or setup
		Employee employee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();

		// when - action or behaviour that we are going test
		// mockMvc: REST API 호출하기 위해 사용
		ResultActions response = mockMvc.perform(post("/api/employees")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(employee)));

		// then - verify the result or output using assert statements
		// JSON 값이 포함되어 있는지 여부 확인 (예상 JSON 값이 올바른 실제 JSON 값을 검증할 것인지)
		response.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
			.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}

	@Test
	@DisplayName("JUnit test for Get All employees REST API")
	void given_when_then() throws Exception {
		// given - precondition or setup
		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees.add(Employee.builder().firstName("Jaeyeon").lastName("Cho").email("cjyeon1022@gmail.com").build());
		listOfEmployees.add(Employee.builder().firstName("Brin").lastName("Sergey").email("cjyeon1022@google.com").build());
		employeeRepository.saveAll(listOfEmployees);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/employees"));

		// then - verify the result or output using assert statements
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
	}

	@Test
	@DisplayName("JUnit test for GET employee by id REST API (positive scenario - valid employee id)")
	void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup
		Employee employee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();
		employeeRepository.save(employee);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

		// then - verify the result or output using assert statements
		response.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
			.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}

	@Test
	@DisplayName("JUnit test for GET employee by id REST API (negative scenario - valid employee id)")
	void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
		// given - precondition or setup
		long employeeId = 1L;
		Employee employee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();
		employeeRepository.save(employee);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

		// then - verify the result or output using assert statements
		response.andExpect(status().isNotFound())
			.andDo(print());
	}

	@Test
	@DisplayName("JUnit test for update employee REST API - positive scenario")
	void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {
		// given - precondition or setup
		Employee savedEmployee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();
		employeeRepository.save(savedEmployee);

		Employee updatedEmployee = Employee.builder()
			.firstName("Brin")
			.lastName("Sergey")
			.email("Sergey@ggoogle.com")
			.build();

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(updatedEmployee)));

		// then - verify the result or output using assert statements
		response.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
			.andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
	}

	@Test
	@DisplayName("JUnit test for update employee REST API - negative scenario")
	void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
		// given - precondition or setup
		long employeeId = 1L;
		Employee savedEmployee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();
		employeeRepository.save(savedEmployee);

		Employee updatedEmployee = Employee.builder()
			.firstName("Brin")
			.lastName("Sergey")
			.email("Sergey@ggoogle.com")
			.build();

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(updatedEmployee)));

		// then - verify the result or output using assert statements
		response.andExpect(status().isNotFound())
			.andDo(print());
	}

	@Test
	@DisplayName("JUnit test for delete employee REST API")
	void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
		// given - precondition or setup
		Employee savedEmployee = Employee.builder()
			.firstName("Jaeyeon")
			.lastName("Cho")
			.email("cjyeon1022@gmail.com")
			.build();
		employeeRepository.save(savedEmployee);

		// when - action or behaviour that we are going test
		ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

		// then - verify the result or output using assert statements
		response.andExpect(status().isOk())
			.andDo(print());
	}
}
