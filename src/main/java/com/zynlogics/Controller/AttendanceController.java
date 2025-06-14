package com.zynlogics.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zynlogics.DTO.AttendanceDTO;
import com.zynlogics.Entity.Attendance;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Service.AttendanceService;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Check-in: POST /api/attendance/checkin?userId=...&location=...
    @PostMapping("/checkin")
    public ResponseEntity<AttendanceDTO> checkIn(
            @RequestParam Integer userId,
            @RequestParam String location,
            @RequestParam(required = false) MultipartFile file) throws IOException {

        // You will need to get Employee from userId (assume method in service or repository)
        Employee employee = new Employee();
        employee.setEmpId(userId);  // If you want, load full Employee entity here or inject Iemployee repo and fetch it

        AttendanceDTO record = attendanceService.checkIn(employee, location, file);
        return ResponseEntity.ok(record);
    }

    // Check-out: POST /api/attendance/checkout/{recordId}
    @PostMapping("/checkout/{recordId}")
    public ResponseEntity<Attendance> checkOut(
            @PathVariable Long recordId,
            @RequestParam(required = false) MultipartFile file) throws IOException {

        Attendance record = attendanceService.checkOut(recordId, file);
        return ResponseEntity.ok(record);
    }

    // Get all attendance records: GET /api/attendance/all
//    @GetMapping("/all")
//    public ResponseEntity<List<Attendance>> getAllRecords() {
//        List<Attendance> records = attendanceService.getAllRecords();
//        return ResponseEntity.ok(records);
//    }
    
 // GET /api/attendance/all
 // GET /api/attendance/all
    @GetMapping("/all")
    public ResponseEntity<List<AttendanceDTO>> getAllRecords() {
        List<Attendance> records = attendanceService.getAllRecords();

        List<AttendanceDTO> dtos = records.stream().map(record -> {
            AttendanceDTO dto = new AttendanceDTO();
            dto.setId(record.getId());
            dto.setEmployeeId(record.getEmployee().getEmpId()); // assuming Employee has getId()
            dto.setEmployeeName(record.getEmployee().getEname()); // assuming Employee has getName()
            dto.setDate(record.getDate());
            dto.setClockIn(record.getClockIn());
            dto.setClockOut(record.getClockOut());
            dto.setWorkingHours(record.getWorkingHours());
            dto.setStatus(record.getStatus());
            dto.setLocation(record.getLocation());
            dto.setCheckInImageUrl(record.getSetCheckInImageUrl());
            dto.setCheckOutImageUrl(record.getSetCheckOutImageUrl());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    

    // Get attendance records by employee: GET /api/attendance/employee/{userId}
//    @GetMapping("/employee/{userId}")
//    public ResponseEntity<List<Attendance>> getRecordsByEmployee(@PathVariable Integer userId) {
//        Employee employee = new Employee();
//        employee.setEmpId(userId);
//        List<Attendance> records = attendanceService.getRecordsByEmployee(employee);
//        return ResponseEntity.ok(records);
//    }
    
    @GetMapping("/employee/{userId}")
    public ResponseEntity<List<AttendanceDTO>> getRecordsByEmployee(@PathVariable Integer userId) {
        List<AttendanceDTO> records = attendanceService.getRecordsByEmpId(userId);
        return ResponseEntity.ok(records);
    }

    

    // Get attendance records by date: GET /api/attendance/date/{date}
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getRecordsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Attendance> records = attendanceService.getRecordsByDate(date);
        return ResponseEntity.ok(records);
    }

    // Get record by employee and date: GET /api/attendance/record/{userId}/{date}
//    @GetMapping("/record/{userId}/{date}")
//    public ResponseEntity<List<AttendanceDTO>> getRecordByEmployeeAndDate(
//            @PathVariable Integer userId,
//            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//
//        Employee employee = new Employee();
//        employee.setEmpId(userId);
//        List<AttendanceDTO> record = attendanceService.getRecordByEmployeeAndDate(employee, date);
//        return ResponseEntity.ok(record);
//    }

    // Get weekly records by employee: GET /api/attendance/weekly/{userId}?start=yyyy-MM-dd&end=yyyy-MM-dd
    @GetMapping("/weekly/{userId}")
    public ResponseEntity<List<Attendance>> getWeeklyRecords(
            @PathVariable Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        Employee employee = new Employee();
        employee.setEmpId(userId);
        List<Attendance> records = attendanceService.getWeeklyRecords(employee, start, end);
        return ResponseEntity.ok(records);
    }

    // Count attendance by status on a date: GET /api/attendance/count/status?date=yyyy-MM-dd&status=Present
    @GetMapping("/count/status")
    public ResponseEntity<Long> countByStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String status) {

        long count = attendanceService.countByStatus(date, status);
        return ResponseEntity.ok(count);
    }

    // Count attendance by location on a date: GET /api/attendance/count/location?date=yyyy-MM-dd&location=Office
    @GetMapping("/count/location")
    public ResponseEntity<Long> countByLocation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String location) {

        long count = attendanceService.countByLocation(date, location);
        return ResponseEntity.ok(count);
    }

    // Get record by ID: GET /api/attendance/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getById(@PathVariable Long id) {
        return attendanceService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete record by ID: DELETE /api/attendance/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        attendanceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // New endpoint: Get current attendance status by employee: GET /api/attendance/current-status/{empId}
    @GetMapping("/current-status/{empId}")
    public ResponseEntity<Map<String, Object>> getCurrentStatus(@PathVariable Integer empId) {
        Employee employee = new Employee();
        employee.setEmpId(empId);

        LocalDate today = LocalDate.now();
        List<AttendanceDTO> records = attendanceService.getByEmployeeAndDate(employee, today);

        Map<String, Object> status = new HashMap<>();

        if (records != null && !records.isEmpty()) {
            // Assume we take the latest (or first) record
            AttendanceDTO record = records.get(0);

            if (record.getClockOut() == null) {
                // Employee is currently checked in
                status.put("isLoggedIn", true);
                status.put("loginTime", record.getClockIn().toString());
                status.put("loginImage", record.getCheckInImageUrl());
                status.put("logoutTime", null);
                status.put("logoutImage", null);

                long secondsSinceCheckIn = ChronoUnit.SECONDS.between(record.getClockIn(), LocalDateTime.now());
                long totalWorkdaySeconds = 9 * 60 * 60;
                long timeLeftSeconds = Math.max(0, totalWorkdaySeconds - secondsSinceCheckIn);

                status.put("timeLeft", (int) timeLeftSeconds);
                status.put("record", record);
            } else {
                // Already clocked out
                status.put("isLoggedIn", false);
                status.put("loginTime", record.getClockIn().toString());
                status.put("loginImage", record.getCheckInImageUrl());
                status.put("logoutTime", record.getClockOut().toString());
                status.put("logoutImage", record.getCheckOutImageUrl());
                status.put("timeLeft", 0);
                status.put("record", record);
            }
        } else {
            // No record for today
            status.put("isLoggedIn", false);
            status.put("loginTime", null);
            status.put("loginImage", null);
            status.put("logoutTime", null);
            status.put("logoutImage", null);
            status.put("timeLeft", 9 * 60 * 60);
            status.put("record", null);
        }

        return ResponseEntity.ok(status);
    }

}