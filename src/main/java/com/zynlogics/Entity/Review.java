package com.zynlogics.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String reviewText;

    private Float rating;

    private String givenBy;

    @ManyToOne
    @JoinColumn(name = "emp_id") // matches Employee.empId
    private Employee employee;
}
