package com.zynlogics.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.zynlogics.DTO.CompenyDocumentsDTO;
import com.zynlogics.Entity.DocumentsCompeny;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Repository.CompenyDocumentsRepo;
import com.zynlogics.Repository.Iemployee;

import lombok.Data;

@Service
@Data
public class DocumentsCompenyService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api.key}")
    private String apiKey;

    @Value("${supabase.bucket}")
    private String bucket;

    @Autowired
    private CompenyDocumentsRepo docRepo;

    @Autowired
    private Iemployee employeeRepo;

    private String uploadFileToSupabase(MultipartFile file) throws Exception {
        // Generate a unique file name with the original extension
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : ".pdf"; // Default to .pdf if no extension
        String fileName = UUID.randomUUID() + extension;
        String path = "/storage/v1/object/" + bucket + "/" + fileName;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("apikey", apiKey);
        headers.setContentType(MediaType.APPLICATION_PDF); // Assuming PDF files; adjust if needed

        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        RestTemplate restTemplate = new RestTemplate();
        String uploadUrl = supabaseUrl + path;

        restTemplate.put(uploadUrl, entity);
        return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
    }

    @Transactional
    public CompenyDocumentsDTO uploadDocuments(Integer empId, CompenyDocumentsDTO dto, MultipartFile paySlips, MultipartFile pf, MultipartFile form16) {
        // Validate employee
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + empId));

        // Validate at least one file is provided
        if (paySlips == null && pf == null && form16 == null) {
            throw new RuntimeException("At least one document must be provided");
        }

        // Create entity
        DocumentsCompeny doc = new DocumentsCompeny();
        doc.setEmployee(emp);
        doc.setUploadDateTime(LocalDateTime.now());

        // Process and store files in Supabase
        try {
            if (paySlips != null && !paySlips.isEmpty()) {
                String fileUrl = uploadFileToSupabase(paySlips);
                doc.setPaySlips(fileUrl);
            }
            if (pf != null && !pf.isEmpty()) {
                String fileUrl = uploadFileToSupabase(pf);
                doc.setPf(fileUrl);
            }
            if (form16 != null && !form16.isEmpty()) {
                String fileUrl = uploadFileToSupabase(form16);
                doc.setForm16(fileUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Supabase: " + e.getMessage(), e);
        }

        // Save to database
        DocumentsCompeny saved = docRepo.save(doc);

        // Update DTO with saved data
        dto.setDocId(saved.getDocId());
        dto.setEmpId(empId);
        dto.setUploadDateTime(saved.getUploadDateTime().toString());
        dto.setPaySlips(saved.getPaySlips());
        dto.setPf(saved.getPf());
        dto.setForm16(saved.getForm16());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<CompenyDocumentsDTO> getDocumentsByEmployee(Integer empId) {
        return docRepo.findByEmployeeEmpId(empId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CompenyDocumentsDTO getDocumentById(Integer docId) {
        return docRepo.findById(docId).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Transactional(readOnly = true)
    public List<CompenyDocumentsDTO> getAllDocuments() {
        return docRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private CompenyDocumentsDTO toDTO(DocumentsCompeny doc) {
        CompenyDocumentsDTO dto = new CompenyDocumentsDTO();
        dto.setDocId(doc.getDocId());
        dto.setEmpId(doc.getEmployee().getEmpId());
        dto.setPaySlips(doc.getPaySlips());
        dto.setPf(doc.getPf());
        dto.setForm16(doc.getForm16());
        dto.setUploadDateTime(doc.getUploadDateTime().toString());
        return dto;
    }
}