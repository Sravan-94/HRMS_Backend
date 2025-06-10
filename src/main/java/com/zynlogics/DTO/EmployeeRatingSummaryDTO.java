package com.zynlogics.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeRatingSummaryDTO {
    private Integer empId;
    private String ename;
    private Double averageRating;
}
