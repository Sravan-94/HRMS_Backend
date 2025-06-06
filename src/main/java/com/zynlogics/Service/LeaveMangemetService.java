package com.zynlogics.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.zynlogics.DTO.EmployeeDTO;
import com.zynlogics.DTO.LeavesManagementDTO;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Entity.LeavesManagement;
import com.zynlogics.Repository.Iemployee;
import com.zynlogics.Repository.LeaveManagementRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveMangemetService {
	
	@Autowired
	private LeaveManagementRepository leavesRepo;
	
	@Autowired
	private Iemployee employeeRepository;
	public LeavesManagementDTO createLeave(LeavesManagementDTO dto) {
	    Employee employee = employeeRepository.findById(dto.getEmpId())
	        .orElseThrow(() -> new RuntimeException("Employee not found"));

	    // Create and save entity
	    LeavesManagement leave = new LeavesManagement();
	    leave.setLeaveType(dto.getLeaveType());
	    leave.setLeaveDate(dto.getLeaveDate());
	    leave.setEndLeave(dto.getEndDate());
	    leave.setStatus("Pending");
	    leave.setReason(dto.getReason());
	    leave.setEmployee(employee);

	    LeavesManagement savedLeave = leavesRepo.save(leave);

	    // Map entity to DTO
	    LeavesManagementDTO responseDto = new LeavesManagementDTO();
	    responseDto.setLeaveType(savedLeave.getLeaveType());
	    responseDto.setLeaveDate(savedLeave.getLeaveDate());
	    responseDto.setEndDate(savedLeave.getEndLeave());
	    responseDto.setStatus(savedLeave.getStatus());
	    responseDto.setReason(savedLeave.getReason());
	    responseDto.setEmId(savedLeave.getLId()); // Assuming LId is the leave ID
	    responseDto.setEmpId(employee.getEmpId());
	    responseDto.setEmpName(employee.getEname());
	    responseDto.setEmail(employee.getEmail());
	    responseDto.setPhno(employee.getPhone());

	    EmployeeDTO empDto = new EmployeeDTO();
	    empDto.setEmpId(employee.getEmpId());
	    empDto.setEname(employee.getEname());
	    empDto.setEmail(employee.getEmail());
	    empDto.setPhone(employee.getPhone());
	    responseDto.setEmps(empDto);

	    return responseDto;
	}

	public List<LeavesManagementDTO> getAllLeaves() {
	    return leavesRepo.findAll().stream().map(leave -> {
	        LeavesManagementDTO dto = new LeavesManagementDTO();

	        dto.setEmId(leave.getLId()); // assuming leave ID is emId
	        dto.setLeaveType(leave.getLeaveType());
	        dto.setLeaveDate(leave.getLeaveDate());
	        dto.setEndDate(leave.getEndLeave());
	        dto.setReason(leave.getReason());
	        dto.setStatus(leave.getStatus());

	        // Get employee from leave
	        Employee emp = leave.getEmployee();
	        if (emp != null) {
	            dto.setEmpId(emp.getEmpId());
	            dto.setEmpName(emp.getEname());
	            dto.setEmail(emp.getEmail());
	            dto.setPhno(emp.getPhone());

	            // Set nested EmployeeDTO
	            EmployeeDTO empDto = new EmployeeDTO();
	            empDto.setEmpId(emp.getEmpId());
	            empDto.setEname(emp.getEname());
	            empDto.setEmail(emp.getEmail());
	            empDto.setPhone(emp.getPhone());
	            
	            dto.setEmps(empDto);
	        }

	        return dto;
	    }).collect(Collectors.toList());
	}

	    public LeavesManagement acceptLeave(Integer leaveId) {
	        LeavesManagement leave = leavesRepo.findById(leaveId)
	                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + leaveId));
	        leave.setStatus("accepted");
	        return leavesRepo.save(leave);
	    }

	    // Reject leave by ID
	    public LeavesManagement rejectLeave(Integer leaveId) {
	        LeavesManagement leave = leavesRepo.findById(leaveId)
	                .orElseThrow(() -> new RuntimeException("Leave not found with ID: " + leaveId));
	        leave.setStatus("rejected");
	        return leavesRepo.save(leave);
	    }



	    public List<LeavesManagementDTO> getLeavesByEmployeeId(Integer empId) {
	        List<LeavesManagement> leaves = leavesRepo.findByEmployeeEmpId(empId);
	        return leaves.stream().map(leave -> {
	            LeavesManagementDTO dto = new LeavesManagementDTO();
	            Employee emp = leave.getEmployee();

	            // Populate nested EmployeeDTO
	            EmployeeDTO empDTO = new EmployeeDTO(emp);
	            empDTO.setEmpId(emp.getEmpId());
	            empDTO.setEname(emp.getEname());
	            empDTO.setEmail(emp.getEmail());
	            empDTO.setPhone(emp.getPhone());

	            // Populate DTO
	            dto.setLeaveType(leave.getLeaveType());
	            dto.setLeaveDate(leave.getLeaveDate());
	            dto.setEndDate(leave.getEndLeave());
	            dto.setStatus(leave.getStatus());
	            dto.setEmpId(emp.getEmpId());   // Employee ID
	            dto.setEmId(leave.getLId());    // Leave ID
	            dto.setReason(leave.getReason());
	            dto.setEmps(empDTO);
	            dto.setEmpName(emp.getEname());
	            dto.setEmail(emp.getEmail());
	            dto.setPhno(emp.getPhone());
	                // Assuming attendance info
	            return dto;
	        }).collect(Collectors.toList());
	    }

	   

}
