package io.jaeyeon.springboottesting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.jaeyeon.springboottesting.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
