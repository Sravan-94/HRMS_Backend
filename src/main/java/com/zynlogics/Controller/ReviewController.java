package com.zynlogics.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zynlogics.DTO.ReviewDTO;
import com.zynlogics.Service.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/give")
    public ReviewDTO giveReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.giveReview(reviewDTO);
    }

    @GetMapping("/all")
    public List<ReviewDTO> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/employee/{empId}")
    public List<ReviewDTO> getReviewsByEmployeeId(@PathVariable Integer empId) {
        return reviewService.getReviewsByEmployeeId(empId);
    }
}
