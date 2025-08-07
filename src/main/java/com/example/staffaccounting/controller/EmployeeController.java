package com.example.staffaccounting.controller;

import com.example.staffaccounting.model.Employee;
import com.example.staffaccounting.model.dto.EmployeeRequest;
import com.example.staffaccounting.model.projection.EmployeeProjection;
import com.example.staffaccounting.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

/**
 * @author Anatoliy Shikin
 */
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee created = employeeService.create(employeeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeService.update(id, employeeRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findEmployeeByIdOrThrow(id));
    }

    @GetMapping("/projection/{lastName}")
    public ResponseEntity<List<EmployeeProjection>> findProjectionsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(employeeService.findByLastName(lastName));
    }

    @GetMapping("/projection")
    public ResponseEntity<List<EmployeeProjection>> findAllProjections() {
        return ResponseEntity.ok(employeeService.findAllProjections());
    }

    @GetMapping("/projection/{id}")
    public ResponseEntity<EmployeeProjection> findProjectionsById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findEmployeeProjectionsById(id));
    }

}
