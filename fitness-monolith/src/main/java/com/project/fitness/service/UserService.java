package com.project.fitness.service;

import com.project.fitness.dto.RegisteredRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserResponse register(RegisteredRequest registeredRequest) {
        User user = modelMapper.map(registeredRequest,User.class);
        userRepository.save(user);
        return modelMapper.map(user,UserResponse.class);
    }
}
