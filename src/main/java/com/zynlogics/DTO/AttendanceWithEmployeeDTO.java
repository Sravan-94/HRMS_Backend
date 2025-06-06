package com.zynlogics.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class AttendanceWithEmployeeDTO {
    private Integer attendanceId;
    private LocalDateTime checkindate;
    private LocalDateTime checkOutdate;
    private String status;

    private Integer empId;
    private String ename;
    private String email;
    private Long phnone;
    private String eployeeId;

    // Getters and setters
}
