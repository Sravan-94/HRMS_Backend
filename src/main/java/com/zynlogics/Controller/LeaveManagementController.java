package com.zynlogics.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zynlogics.DTO.LeavesManagementDTO;
import com.zynlogics.Entity.LeavesManagement;
import com.zynlogics.Service.LeaveMangemetService;
@RequestMapping("/leaves")
@RestController
//@CrossOrigin(origins = "http://localhost:5173") 
public class LeaveManagementController {

	    @Autowired
	    private LeaveMangemetService leavesService;
	    @PostMapping("/postemployeleaves")
	    public ResponseEntity<LeavesManagementDTO> requestLeave(@RequestBody LeavesManagementDTO dto) {
	        LeavesManagementDTO createdLeave = leavesService.createLeave(dto);
	       
	        return new ResponseEntity<>(createdLeave, HttpStatus.CREATED);

	    }
	    
	    
	    @GetMapping("/getAllLeaves")
	    public ResponseEntity<List<LeavesManagementDTO>> getAllLeaves() {
	        List<LeavesManagementDTO> leaves = leavesService.getAllLeaves();
	        return ResponseEntity.ok(leaves);
	    }
	    
	    @PutMapping("/{leaveId}/accept")
	    public ResponseEntity<LeavesManagement> acceptLeave(@PathVariable Integer leaveId) {
	        LeavesManagement updatedLeave = leavesService.acceptLeave(leaveId);
	        return ResponseEntity.ok(updatedLeave);
	    }

	    // Reject leave
	    @PutMapping("/{leaveId}/reject")
	    public ResponseEntity<LeavesManagement> rejectLeave(@PathVariable Integer leaveId) {
	        LeavesManagement updatedLeave = leavesService.rejectLeave(leaveId);
	        return ResponseEntity.ok(updatedLeave);
	    }

	    @GetMapping("/employee/{empId}")
	    public ResponseEntity<List<LeavesManagementDTO>> getLeavesByEmployeeId(@PathVariable Integer empId) {
	        List<LeavesManagementDTO> leaves = leavesService.getLeavesByEmployeeId(empId);
	        return ResponseEntity.ok(leaves);
	    }
	  
	    
	    
}
