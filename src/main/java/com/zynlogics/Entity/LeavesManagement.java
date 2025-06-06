package com.zynlogics.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table
public class LeavesManagement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer lId;
	private String leaveType;
	private LocalDateTime leaveDate;
	private LocalDateTime endLeave;
	
	@Column(columnDefinition = "TEXT")
	private String reason;

    private String status;
	
//	   @ManyToOne
//	    @JoinColumn(name = "employee_id")
//	    private Employee employee;
	   
	   @ManyToOne
	    @JoinColumn(name = "employee_id")
	    @JsonBackReference
	    private Employee employee;
}
