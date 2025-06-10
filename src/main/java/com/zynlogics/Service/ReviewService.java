package com.zynlogics.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zynlogics.DTO.ReviewDTO;
import com.zynlogics.Entity.Employee;
import com.zynlogics.Entity.Review;
import com.zynlogics.Repository.Iemployee;
import com.zynlogics.Repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private Iemployee employeeRepo;

    public ReviewDTO giveReview(ReviewDTO dto) {
        Employee emp = employeeRepo.findById(dto.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Review review = new Review();
        review.setReviewText(dto.getReviewText());
        review.setRating(dto.getRating());
        review.setGivenBy(dto.getGivenBy());
        review.setEmployee(emp);

        Review saved = reviewRepo.save(review);
        dto.setId(saved.getId());
        return dto;
    }

    public List<ReviewDTO> getReviewsByEmployeeId(Integer empId) {
        return reviewRepo.findByEmployeeEmpId(empId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getAllReviews() {
        return reviewRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setEmpId(review.getEmployee().getEmpId());
        dto.setRating(review.getRating());
        dto.setReviewText(review.getReviewText());
        dto.setGivenBy(review.getGivenBy());
        return dto;
    }
}
