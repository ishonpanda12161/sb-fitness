package com.project.fitness.service;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final ModelMapper modelMapper;
    public Recommendation generate(RecommendationRequest request,String userId, String activityId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not found"));
        Activity activity = activityRepository.findById(activityId).orElseThrow(()-> new RuntimeException("Activity Not found"));

        Recommendation recommendation = modelMapper.map(request,Recommendation.class);
        recommendation.setUser(user);
        recommendation.setActivity(activity);
        return recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId).stream().toList();
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId).stream().toList();
    }
}
