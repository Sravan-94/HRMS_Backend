package com.zynlogics.DTO;

import lombok.Data;

@Data
public class ReviewDTO {
    private Integer id;
    private Integer empId;
    private String reviewText;
    private Float rating;
    private String givenBy;
}
