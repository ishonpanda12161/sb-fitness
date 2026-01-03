package com.project.fitness.service;

import com.project.fitness.dto.RegisteredRequest;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(RegisteredRequest registeredRequest) {

        userRepository.save(user);
        return user;
    }
}
