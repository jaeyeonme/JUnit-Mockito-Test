package io.jaeyeon.springboottesting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jaeyeon.springboottesting.model.Employee;
import io.jaeyeon.springboottesting.service.EmployeeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	@PostMapping
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		Employee savedEmployee = employeeService.saveEmployee(employee);
		return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}

	@GetMapping
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long employeeId) {
		return employeeService.getEmployeeById(employeeId)
			// map: Optional 객체에서 ResponseEntity 객체로 변환하는 함수를 적용
			.map(ResponseEntity::ok)
			// orElseGet: ResponseEntity.notFound()를 호출하여 HTTP 404 Not Found 상태 코드를 가지는 응답 객체를 반환
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody Employee employee) {
		return employeeService.getEmployeeById(employeeId)
			.map(savedEmployee -> {
				Employee updatedEmployee = Employee.builder()
					.firstName(employee.getFirstName())
					.lastName(employee.getLastName())
					.email(employee.getEmail())
					.build();

				updatedEmployee = employeeService.updateEmployee(updatedEmployee);
				return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
			})
			.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {

		employeeService.deleteEmployee(employeeId);

		return new ResponseEntity<>("Employee deleted success!", HttpStatus.OK);
	}
}
