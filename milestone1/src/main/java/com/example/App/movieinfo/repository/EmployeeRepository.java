package com.example.App.movieinfo.repository;

import com.example.App.movieinfo.model.Employee;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository @Primary
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}