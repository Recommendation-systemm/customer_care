package com.customer.care.services;

import com.customer.care.entities.Department;
import com.customer.care.entities.SubDepartment;
import com.customer.care.repositories.DepartmentRepository;
import com.customer.care.repositories.SubDepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final SubDepartmentRepository subDepartmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository, SubDepartmentRepository subDepartmentRepository) {
        this.departmentRepository = departmentRepository;
        this.subDepartmentRepository = subDepartmentRepository;
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public SubDepartment createSubDepartment(SubDepartment subDepartment) {
        return subDepartmentRepository.save(subDepartment);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}

