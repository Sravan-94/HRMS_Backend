package com.zynlogics.DTO;

import lombok.Data;

@Data
public class AttendenceRequest {
    private Long userId;
    private String image; // Base64 image
}
