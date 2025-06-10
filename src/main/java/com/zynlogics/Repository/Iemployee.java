package com.zynlogics.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zynlogics.Entity.Attendance;
import com.zynlogics.Entity.Employee;
@Repository
public interface Iemployee extends JpaRepository<Employee,Integer> , CrudRepository<Employee, Integer>{

	Optional<Employee> findByEmailAndPassword(String email, String password);
	Optional<Employee> findById(Integer empId);

}

