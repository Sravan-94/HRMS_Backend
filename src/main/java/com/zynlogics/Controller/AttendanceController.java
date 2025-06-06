package com.zynlogics.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAllRecords() {
        List<Attendance> records = attendanceService.getAllRecords();
        return ResponseEntity.ok(records);
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
    public ResponseEntity<List<Attendance>> getRecordsByEmployee(@PathVariable Integer userId) {
        List<Attendance> records = attendanceService.getRecordsByEmpId(userId);
        if (records.isEmpty()) {
            System.out.println("No records found for userId: " + userId); // Debug log
        }
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
    @GetMapping("/record/{userId}/{date}")
    public ResponseEntity<Attendance> getRecordByEmployeeAndDate(
            @PathVariable Integer userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Employee employee = new Employee();
        employee.setEmpId(userId);
        Attendance record = attendanceService.getRecordByEmployeeAndDate(employee, date);
        return ResponseEntity.ok(record);
    }

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

        // Fetch the attendance record for today for this employee
        LocalDate today = LocalDate.now();
        Attendance record = attendanceService.getRecordByEmployeeAndDate(employee, today);

        Map<String, Object> status = new HashMap<>();

        if (record != null && record.getClockOut() == null) {
            // Employee is currently checked in
            status.put("isLoggedIn", true);
            status.put("loginTime", record.getClockIn().toString());
            status.put("loginImage", record.getSetCheckInImageUrl());
            status.put("logoutTime", null);
            status.put("logoutImage", null);
            // Calculate timeLeft (assuming a 9-hour workday)
            long secondsSinceCheckIn = ChronoUnit.SECONDS.between(record.getClockIn(), LocalDateTime.now());
            long totalWorkdaySeconds = 9 * 60 * 60; // 9 hours in seconds
            long timeLeftSeconds = Math.max(0, totalWorkdaySeconds - secondsSinceCheckIn);
            status.put("timeLeft", (int) timeLeftSeconds);
            status.put("record", record);
            return ResponseEntity.ok(status);
        } else {
            // Employee is not checked in
            status.put("isLoggedIn", false);
            status.put("loginTime", null);
            status.put("loginImage", null);
            status.put("logoutTime", record != null ? record.getClockOut().toString() : null);
            status.put("logoutImage", record != null ? record.getSetCheckOutImageUrl() : null);
            status.put("timeLeft", 9 * 60 * 60); // Reset to 9 hours
            status.put("record", record);
            return ResponseEntity.ok(status);
        }
    }
}