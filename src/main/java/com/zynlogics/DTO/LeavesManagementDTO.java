package com.zynlogics.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeavesManagementDTO {
    private Integer lId;
    private String leaveType;
    private LocalDateTime leaveDate;
    private LocalDateTime endDate;
    private Integer empId;
    private Integer emId; // leave ID
    private String Status;
    private EmployeeDTO emps;
    private String empName;
    private String email;
    private Long phno;
    private String att;
    private String reason;
    // Assuming you just want to pass the employee ID
}
