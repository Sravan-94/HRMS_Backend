package com.zynlogics.DTO;

public class CompenyDocumentsDTO {
    private Integer docId;
    private Integer empId;
    private String paySlips;  // Will store a URL like /documents/download/{docId}/paySlips
    private String pf;        // Will store a URL like /documents/download/{docId}/pf
    private String form16;    // Will store a URL like /documents/download/{docId}/form16
    private String uploadDateTime;

    // Getters and setters
    public Integer getDocId() { return docId; }
    public void setDocId(Integer docId) { this.docId = docId; }
    public Integer getEmpId() { return empId; }
    public void setEmpId(Integer empId) { this.empId = empId; }
    public String getPaySlips() { return paySlips; }
    public void setPaySlips(String paySlips) { this.paySlips = paySlips; }
    public String getPf() { return pf; }
    public void setPf(String pf) { this.pf = pf; }
    public String getForm16() { return form16; }
    public void setForm16(String form16) { this.form16 = form16; }
    public String getUploadDateTime() { return uploadDateTime; }
    public void setUploadDateTime(String uploadDateTime) { this.uploadDateTime = uploadDateTime; }
}