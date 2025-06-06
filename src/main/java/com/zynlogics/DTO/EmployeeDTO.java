package com.zynlogics.DTO;

import com.zynlogics.Entity.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
	
	 	private Integer empId;
	    private String ename;
	    private String email;
	    private String password;
	    private Long phone;
//	    private String employeeid;
	    private String role;
//	    private LeavesManagementDTO leavedto;
//	    private AttendanceDTO attendancedto;
	    public EmployeeDTO(Employee employee) {
	        this.empId = employee.getEmpId();
	        this.ename = employee.getEname();
	        this.email = employee.getEmail();
	        this.password = employee.getPassword();
	        this.phone = employee.getPhone();
	        this.role = employee.getRole();
	    }
	    
}
