package com.zynlogics.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.zynlogics.DTO.AttendanceDTO;
import com.zynlogics.Entity.Attendance;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Repository.IAttendancerepo;


@Service
public class AttendanceService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api.key}")
    private String apiKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Autowired
    private IAttendancerepo attendanceRecordRepository;

    private String uploadImageToSupabase(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + ".jpg";
        String path = "/storage/v1/object/" + bucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("apikey", apiKey);
        headers.setContentType(MediaType.IMAGE_JPEG);

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        RestTemplate restTemplate = new RestTemplate();
        String uploadUrl = supabaseUrl + path;

        restTemplate.put(uploadUrl, entity);
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
    }

    public AttendanceDTO checkIn(Employee employee, String location, MultipartFile file) throws IOException {
        Attendance record = new Attendance();
        record.setEmployee(employee);
        record.setDate(LocalDate.now());
        record.setClockIn(LocalDateTime.now());
        record.setLocation(location);
        record.setStatus("Present");

        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadImageToSupabase(file);
            record.setSetCheckInImageUrl(imageUrl);
        }

        Attendance savedRecord = attendanceRecordRepository.save(record);
        return convertToDTO(savedRecord);
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        Double hoursWorked = null;
        if (attendance.getClockIn() != null && attendance.getClockOut() != null) {
            hoursWorked = (double) Duration.between(
                attendance.getClockIn(), attendance.getClockOut()
            ).toMinutes() / 60;
        }

        return new AttendanceDTO(
            attendance.getId(),
            attendance.getEmployee().getEmpId(),
            attendance.getEmployee().getEname(),
            attendance.getDate(),
            attendance.getClockIn(),
            attendance.getClockOut(),
            hoursWorked,
            attendance.getStatus(),
            attendance.getLocation(),
            attendance.getSetCheckInImageUrl(),
            attendance.getSetCheckOutImageUrl()
        );
    }

    public Attendance checkOut(Long recordId, MultipartFile file) throws IOException {
        Attendance record = attendanceRecordRepository.findById(recordId)
            .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        record.setClockOut(LocalDateTime.now());

        if (record.getClockIn() != null) {
            Duration duration = Duration.between(record.getClockIn(), record.getClockOut());
        }

        if (file != null && !file.isEmpty()) {
            String imageUrl = uploadImageToSupabase(file);
            record.setSetCheckOutImageUrl(imageUrl);
        }

        return attendanceRecordRepository.save(record);
    }

    // Get all attendance records
    public List<Attendance> getAllRecords() {
        return attendanceRecordRepository.findAll();
    }

    // Get records by employee
    public List<Attendance> getRecordsByEmployee(Employee employee) {
        return attendanceRecordRepository.findByEmployee(employee);
    }

    // Get records by date
    public List<Attendance> getRecordsByDate(LocalDate date) {
        return attendanceRecordRepository.findByDate(date);
    }

    // Get specific employee record on a given date
    public Attendance getRecordByEmployeeAndDate(Employee employee, LocalDate date) {
        return attendanceRecordRepository.findByEmployeeAndDate(employee, date);
    }

    // Get all records for an employee within a date range (for weekly summary)
    public List<Attendance> getWeeklyRecords(Employee employee, LocalDate startDate, LocalDate endDate) {
        return attendanceRecordRepository.findByEmployeeAndDateBetween(employee, startDate, endDate);
    }

    // Count attendance status for a specific day
    public long countByStatus(LocalDate date, String status) {
        return attendanceRecordRepository.countByDateAndStatus(date, status);
    }

    // Count location-based attendance for a specific day
    public long countByLocation(LocalDate date, String location) {
        return attendanceRecordRepository.countByDateAndLocation(date, location);
    }

    // Get record by ID (optional for admin editing)
    public Optional<Attendance> getById(Long id) {
        return attendanceRecordRepository.findById(id);
    }

    // Delete a record (optional)
    public void deleteById(Long id) {
        attendanceRecordRepository.deleteById(id);
    }

    // Fix: Implement getRecordsByEmpId to fetch records by empId directly
    public List<Attendance> getRecordsByEmpId(Integer userId) {
        List<Attendance> records = attendanceRecordRepository.findByEmployeeEmpId(userId);
        System.out.println("Records found for empId " + userId + ": " + records.size()); // Debug log
        return records;
    }

    // Remove the old stub method with incorrect signature
    // public List<Attendance> getRecordsByEmployee(Integer userId) {
    //     return null;
    // }
}