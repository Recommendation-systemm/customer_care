package com.customer.care.controllers;

import com.customer.care.entities.Department;
import com.customer.care.entities.SubDepartment;
import com.customer.care.services.DepartmentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @PostMapping("/{departmentId}/subdepartments")
    public SubDepartment createSubDepartment(@PathVariable Long departmentId, @RequestBody SubDepartment subDepartment) {
        Department department = departmentService.getAllDepartments()
                .stream()
                .filter(dep -> dep.getId().equals(departmentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Department not found"));

        subDepartment.setParentDepartment(department);
        return departmentService.createSubDepartment(subDepartment);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
}
