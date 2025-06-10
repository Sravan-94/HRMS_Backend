package com.zynlogics.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zynlogics.Entity.DocumentsCompeny;
import com.zynlogics.Entity.Employee;

public interface CompenyDocumentsRepo extends JpaRepository<DocumentsCompeny, Integer> {
    List<DocumentsCompeny> findByEmployeeEmpId(Integer empId);

	
}
