package com.zynlogics.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zynlogics.Entity.LeavesManagement;

@Repository
public interface LeaveManagementRepository extends JpaRepository<LeavesManagement, Integer> {
    List<LeavesManagement> findByEmployeeEmpId(Integer empId);
}
