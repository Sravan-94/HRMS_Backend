package com.zynlogics.DTO;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class CompenyDocumentsDTO {
	private Integer docId;
	 private String PaySlips;
	 private String pf;
	 private String form16;
	 @Transient
	 private EmployeeDTO emps;
}
