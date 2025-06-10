package com.zynlogics.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zynlogics.Entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByEmployeeEmpId(Integer empId); // Works correctly because of field name in Employee
}
