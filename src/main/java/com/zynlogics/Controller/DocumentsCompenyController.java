package com.zynlogics.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zynlogics.DTO.CompenyDocumentsDTO;
import com.zynlogics.Service.DocumentsCompenyService;

@RestController
@RequestMapping("/documents")
public class DocumentsCompenyController {

    @Autowired
    private DocumentsCompenyService docService;

    // Admin uploads document for employee
    @PostMapping(value = "/upload/{empId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CompenyDocumentsDTO> uploadDocs(
            @PathVariable Integer empId,
            @RequestPart(value = "paySlips", required = false) MultipartFile paySlips,
            @RequestPart(value = "pf", required = false) MultipartFile pf,
            @RequestPart(value = "form16", required = false) MultipartFile form16) {
        if (paySlips == null && pf == null && form16 == null) {
            return ResponseEntity.badRequest().body(null);
        }
        CompenyDocumentsDTO dto = new CompenyDocumentsDTO();
        dto.setEmpId(empId);
        CompenyDocumentsDTO result = docService.uploadDocuments(empId, dto, paySlips, pf, form16);
        return ResponseEntity.ok(result);
    }

    // Serve file content from database
    @GetMapping(value = "/download/{docId}/{type}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Integer docId, @PathVariable String type) {
        CompenyDocumentsDTO doc = docService.getDocumentById(docId);
        byte[] fileContent = null;
        String fileName = null;

        switch (type.toLowerCase()) {
            case "payslips":
                fileContent = docService.getDocumentById(docId).getPaySlips() != null ?
                    docService.getDocumentById(docId).getPaySlips().getBytes() : null;
                fileName = "PaySlip.pdf";
                break;
            case "pf":
                fileContent = docService.getDocumentById(docId).getPf() != null ?
                    docService.getDocumentById(docId).getPf().getBytes() : null;
                fileName = "PF_Statement.pdf";
                break;
            case "form16":
                fileContent = docService.getDocumentById(docId).getForm16() != null ?
                    docService.getDocumentById(docId).getForm16().getBytes() : null;
                fileName = "Form16.pdf";
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }

        if (fileContent == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .body(fileContent);
    }

    // Employee fetches their documents
    @GetMapping("/employee/{empId}")
    public List<CompenyDocumentsDTO> getDocsByEmployee(@PathVariable Integer empId) {
        return docService.getDocumentsByEmployee(empId);
    }

    // Get a document record by ID (returns DTO with URLs)
    @GetMapping("/{docId}")
    public CompenyDocumentsDTO getDoc(@PathVariable Integer docId) {
        return docService.getDocumentById(docId);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<CompenyDocumentsDTO>> getAllDocuments() {
        List<CompenyDocumentsDTO> docs = docService.getAllDocuments();
        return ResponseEntity.ok(docs);
    }
}