package com.zynlogics.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.Data;

@Entity
@Data
@Transactional
@Table(name="Employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer empId;
	private String ename;
	private String role;
	private String email;
	private String password;
	private Long phone;
//	private String employeeid;
	private Float rating;
	
//     @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
//     private List<LeavesManagement> leaves;

     @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     @JsonManagedReference
     private List<LeavesManagement> leaves;
    
    @OneToMany(mappedBy = "employee")
    @JsonIgnore // or @JsonIgnoreProperties("employee")
    private List<Attendance> attendances;
    
    @OneToMany(mappedBy="employee" ,cascade = CascadeType.ALL)
    private List<DocumentsCompeny>documents;
}
