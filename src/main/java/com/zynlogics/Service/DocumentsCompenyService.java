package com.zynlogics.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zynlogics.DTO.CompenyDocumentsDTO;
import com.zynlogics.Entity.DocumentsCompeny;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Repository.CompenyDocumentsRepo;
import com.zynlogics.Repository.Iemployee;

@Service
public class DocumentsCompenyService {

    @Autowired
    private CompenyDocumentsRepo docRepo;

    @Autowired
    private Iemployee employeeRepo;

    public CompenyDocumentsDTO uploadDocuments(Integer empId, CompenyDocumentsDTO dto) {
        Employee emp = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        DocumentsCompeny doc = new DocumentsCompeny();
        doc.setPaySlips(dto.getPaySlips());
        doc.setPf(dto.getPf());
        doc.setForm16(dto.getForm16());
        doc.setUploadDateTime(LocalDateTime.now());
        doc.setEmployee(emp);

        DocumentsCompeny saved = docRepo.save(doc);
        dto.setDocId(saved.getDocId());
        dto.setUploadDateTime(saved.getUploadDateTime());
        dto.setEmpId(empId);
        return dto;
    }

    public List<CompenyDocumentsDTO> getDocumentsByEmployee(Integer empId) {
        return docRepo.findByEmployeeEmpId(empId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CompenyDocumentsDTO getDocumentById(Integer docId) {
        return docRepo.findById(docId).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    private CompenyDocumentsDTO toDTO(DocumentsCompeny doc) {
    	CompenyDocumentsDTO dto = new CompenyDocumentsDTO();
        dto.setDocId(doc.getDocId());
        dto.setEmpId(doc.getEmployee().getEmpId());
        dto.setPaySlips(doc.getPaySlips());
        dto.setPf(doc.getPf());
        dto.setForm16(doc.getForm16());
        dto.setUploadDateTime(doc.getUploadDateTime());
        return dto;
    }
}
