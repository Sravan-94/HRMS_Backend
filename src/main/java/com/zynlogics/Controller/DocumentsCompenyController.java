package com.zynlogics.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zynlogics.DTO.CompenyDocumentsDTO;
import com.zynlogics.Service.DocumentsCompenyService;

@RestController
@RequestMapping("/documents")
public class DocumentsCompenyController {

    @Autowired
    private DocumentsCompenyService docService;

    // Admin uploads document for employee
    @PostMapping("/upload/{empId}")
    public CompenyDocumentsDTO uploadDocs(@PathVariable Integer empId, @RequestBody CompenyDocumentsDTO dto) {
        return docService.uploadDocuments(empId, dto);
    }

    // Employee fetches their documents
    @GetMapping("/employee/{empId}")
    public List<CompenyDocumentsDTO> getDocsByEmployee(@PathVariable Integer empId) {
        return docService.getDocumentsByEmployee(empId);
    }

    // Download a document record by ID (returns file paths stored)
    @GetMapping("/{docId}")
    public CompenyDocumentsDTO getDoc(@PathVariable Integer docId) {
        return docService.getDocumentById(docId);
    }
}
