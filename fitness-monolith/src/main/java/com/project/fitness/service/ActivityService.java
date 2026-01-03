package com.project.fitness.service;


import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ActivityResponse createActivity(ActivityRequest activityRequest, String userId) {

        User user = userRepository.findById(userId).orElse(null);
        Activity activity = modelMapper.map(activityRequest,Activity.class);
        activity.setUser(user);
        ActivityResponse activityResponse = modelMapper.map(activityRepository.save(activity),ActivityResponse.class);
        activityResponse.setUserId(userId);
        return activityResponse;

    }

    public List<ActivityResponse> getAllActivities(String userId) {

        List<Activity> activityList = activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(activity -> modelMapper.map(activity,ActivityResponse.class))
                .toList();
    }
}
