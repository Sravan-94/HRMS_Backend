package com.zynlogics.Repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zynlogics.Entity.Attendance;
import com.zynlogics.Entity.Employee;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface IAttendancerepo extends JpaRepository<Attendance, Long> {

    

	List<Attendance> findByEmployee(Employee employee);

	List<Attendance> findByDate(LocalDate date);

	List<Attendance> findByEmployeeAndDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);

	long countByDateAndStatus(LocalDate date, String status);

	long countByDateAndLocation(LocalDate date, String location);
	
	List<Attendance> findByEmployeeEmpId(Integer empId);
	
	List<Attendance> findByEmployeeEmpIdOrderByDateDesc(Integer empId);
	@EntityGraph(attributePaths = "employee")
	List<Attendance> findByEmployeeAndDate(Employee employee, LocalDate date);


}



