package com.project.fitness.controller;

import com.project.fitness.dto.RegisteredRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> getUsers(@RequestBody RegisteredRequest registeredRequest)
    {
        UserResponse userResponse = userService.register(registeredRequest);
        return ResponseEntity.ok(userResponse);
    }

}
