package io.jaeyeon.springboottesting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jaeyeon.springboottesting.exception.ResourceNotFoundException;
import io.jaeyeon.springboottesting.model.Employee;
import io.jaeyeon.springboottesting.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepository employeeRepository;

	public Employee saveEmployee(Employee employee) {
		if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
			throw new ResourceNotFoundException("Employee already exist with given email:" + employee.getEmail());
		}

		return employeeRepository.save(employee);
	}

	@Transactional(readOnly = true)
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Employee> getEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee updateEmployee(Employee updatedEmployee) {
		return employeeRepository.save(updatedEmployee);
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}
}
