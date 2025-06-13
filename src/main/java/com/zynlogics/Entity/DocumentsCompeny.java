package com.zynlogics.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class DocumentsCompeny {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docId;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    private String paySlips;

    private String pf;

    private String form16;

    private LocalDateTime uploadDateTime;

    // Getters and setters

}