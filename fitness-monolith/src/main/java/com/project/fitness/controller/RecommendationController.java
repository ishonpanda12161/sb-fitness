package com.project.fitness.controller;


import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.model.Recommendation;
import com.project.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<Recommendation> generateRecommendation(
        @RequestBody RecommendationRequest request,
        @RequestHeader String userId,
        @RequestHeader String activityId
    ){
        Recommendation recommendation = recommendationService.generate(request,userId,activityId);
        return ResponseEntity.ok(recommendation);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Recommendation>> getUserRecommendation(
            @RequestHeader String userId
    )
    {
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    @GetMapping("/activity")
    public ResponseEntity<List<Recommendation>> getActivityRecommendation(
            @RequestHeader String activityId
    )
    {
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }

}
