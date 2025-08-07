package com.example.staffaccounting.repository;

import com.example.staffaccounting.model.Employee;
import com.example.staffaccounting.model.projection.EmployeeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Anatoliy Shikin
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select e.firstName, e.lastName, e.position, e.department.name " +
            " from Employee e")
    List<EmployeeProjection> findAllProjections();

    @Query("select e.firstName, e.lastName, e.position, e.department.name " +
            " from Employee e where e.lastName = :lastName")
    List<EmployeeProjection> findProjectsByLastName(String lastName);

    @Query("select e.firstName, e.lastName, e.position, e.department.name " +
            " from Employee e where e.id = :id")
    Optional<EmployeeProjection> findProjectsById(Long id);
}
