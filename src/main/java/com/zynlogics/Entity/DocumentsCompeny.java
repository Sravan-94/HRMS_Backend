package com.zynlogics.Entity;

import java.time.LocalDateTime;

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
@Table(name="CompenyDocuments")
public class DocumentsCompeny {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	 private Integer docId;
	 private String PaySlips;
	 private String pf;
	 private String form16;
	 private LocalDateTime uploadDateTime;
	 
	 @ManyToOne
	    @JoinColumn(name = "employee_id")
	    private Employee employee;
	

}
