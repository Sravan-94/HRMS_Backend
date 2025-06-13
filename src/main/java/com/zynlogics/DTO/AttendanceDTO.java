package com.zynlogics.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.zynlogics.Entity.Attendance;
import com.zynlogics.Entity.Employee;

import lombok.Data;
@Data

public class AttendanceDTO {
	private Long id;
    private Integer employeeId;         // You can replace with String if you're using email or something else
    private String employeeName;        // Optional: helpful for displaying on frontend
    private LocalDate date;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private Double hoursWorked;         // Calculated from clockIn & clockOut
    private String status;
    private String location;
    private String checkInImageUrl;
    private String checkOutImageUrl;
    
private Integer empId;
	
	private Double workingHours;
	
	
	
	
	
}
