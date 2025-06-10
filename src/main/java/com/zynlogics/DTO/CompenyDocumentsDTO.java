package com.zynlogics.DTO;

import java.time.LocalDateTime;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class CompenyDocumentsDTO {
    private Integer docId;
    private Integer empId;
    private String paySlips;
    private String pf;
    private String form16;
    private LocalDateTime uploadDateTime;


	
	
}
