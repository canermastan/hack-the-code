package com.kodla.coz.controller;

import com.kodla.coz.repository.FeedbackRepository;
import com.kodla.coz.model.Feedback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class FeedbackController {
    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    private final FeedbackRepository feedbackRepository;

    /**
     * SaveFeedback metodu gönderilen feedback'leri veri tabanına kaydeden ve email gönderen metottur
     * @param feedback
     * @return ResponseEntity
     */
    @PostMapping(value = "/feedback", consumes = "application/json")
    public ResponseEntity<?> saveFeedback(@RequestBody Feedback feedback){
        feedback.setCreatedDate(LocalDateTime.now());
        feedbackRepository.saveFeedback(feedback);
        return ResponseEntity.ok().build();
    }

}
