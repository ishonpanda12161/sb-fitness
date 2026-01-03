package com.project.fitness.controller;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<ActivityResponse> trackActivity(
            @RequestBody ActivityRequest activityRequest,
            @RequestHeader String userId
    )
    {
        return ResponseEntity.ok(activityService.createActivity(activityRequest,userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ActivityResponse>> getUserActivities(
            @RequestHeader(name = "USER_ID") String userId
    )
    {
        return ResponseEntity.ok(activityService.getAllActivities(userId));
    }
}
