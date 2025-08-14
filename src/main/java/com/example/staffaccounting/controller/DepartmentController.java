package com.example.staffaccounting.controller;

import com.example.staffaccounting.model.Department;
import com.example.staffaccounting.model.dto.DepartmentRequest;
import com.example.staffaccounting.model.projection.DepartmentProjection;
import com.example.staffaccounting.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MODERATOR','SUPER_ADMIN')")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody DepartmentRequest departmentRequest) {
        Department created = departmentService.createDepartment(departmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('MODERATOR','SUPER_ADMIN')")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest departmentRequest
    ) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','SUPER_ADMIN')")
    public ResponseEntity<Department> findDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findDepartmentByIdOrThrow(id));
    }

    @GetMapping("/projections")
    @PreAuthorize("hasAnyRole('USER','MODERATOR','SUPER_ADMIN')")
    public ResponseEntity<List<DepartmentProjection>> findAllDepartmentProjections() {
        return ResponseEntity.ok(departmentService.findAllDepartmentProjections());
    }
}
