package com.zynlogics.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zynlogics.DTO.EmployeeDTO;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Service.EmployeeImpl;

@RestController
@RequestMapping("/emps")
//@CrossOrigin(origins = "http://localhost:8081") 
public class EmployeeController {

	@Autowired
	private EmployeeImpl empservice;
	
	
	@PostMapping("/login")
	public ResponseEntity<EmployeeDTO> login(@RequestBody Map<String, String> loginData) {
	    String email = loginData.get("email");
	    String password = loginData.get("password");

	    try {
	        Employee employee = empservice.authenticateEmployee(email, password);
	        EmployeeDTO employeeDTO = new EmployeeDTO(employee);
	        return ResponseEntity.ok(employeeDTO);
	    } catch (RuntimeException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
	}


	
	 // CREATE
	@PostMapping("/saveEmp")
	public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDTO dto) {
	    Employee savedEmployee = empservice.uploadEmp(dto);
	    return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
	}
	    

	    @PutMapping("/{id}/rating")
	    public ResponseEntity<Employee> updateRating(@PathVariable Integer id, @RequestParam Float rating) {
	        Employee updatedEmployee = empservice.updateRating(id, rating);
	        return ResponseEntity.ok(updatedEmployee);
	    }
	    
//	    @GetMapping("/{id}")
//	    public EmployeeDTO getEmployee(@PathVariable Integer id) {
//	        return empservice.getEmployeeDTOById(id);
//	    }
	
    // READ ALL
	@GetMapping("/getEmps")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
	    List<Employee> allEmps = empservice.getAllEmps();
	    List<EmployeeDTO> dtoList = new ArrayList<>();

        for (Employee att : allEmps) {
           
        	EmployeeDTO dto=new EmployeeDTO(att);
        	dto.setEname(att.getEname());
        	dto.setEmail(att.getEmail());
        	dto.setEmpId(att.getEmpId());
        	dto.setPhone(att.getPhone());
        	

            dtoList.add(dto);
        }

        return ResponseEntity.ok(dtoList);
	}
//	@DeleteMapping("/deleteEmp/{id}")
//	public 

}
