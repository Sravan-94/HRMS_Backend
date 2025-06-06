package com.zynlogics.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zynlogics.DTO.EmployeeDTO;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Repository.Iemployee;

@Service
public class EmployeeImpl {

    @Autowired
    private Iemployee empRepository;

    public Employee updateRating(Integer id, Float rating) {
        Employee employee = empRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        employee.setRating(rating);
        return empRepository.save(employee);
    }



    public List<Employee> getAllEmps() {
        return empRepository.findAll();
    }

    public Employee authenticateEmployee(String email, String password) {
        return empRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }
    
    
    public Employee uploadEmp(EmployeeDTO dto) {
        Employee employee;

        // Check if we're updating an existing employee
        if (dto.getEmpId() != null && empRepository.existsById(dto.getEmpId())) {
            employee = empRepository.findById(dto.getEmpId())
                    .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + dto.getEmpId()));
        } else {
            employee = new Employee(); // Create new employee
        }

        employee.setEname(dto.getEname());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setPhone(dto.getPhone());
        employee.setRole(dto.getRole());

        return empRepository.save(employee);
    }

}
