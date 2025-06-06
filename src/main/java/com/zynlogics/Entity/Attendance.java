package com.zynlogics.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "employee_id")  // FK column
//    private Employee employee;

    private LocalDate date;

    private LocalDateTime clockIn;

    private LocalDateTime clockOut;

//    private double hoursWorked;

    // e.g., "Present", "Late", "Absent", "On Leave"
    private String status;

    // e.g., "Remote", "Office"
    private String location;
    
    private String setCheckInImageUrl;
 // In Attendance.java
    @ManyToOne
    @JsonIgnoreProperties("attendances") // ignore the 'attendances' list in Employee
    private Employee employee;

	private String setCheckOutImageUrl;}
