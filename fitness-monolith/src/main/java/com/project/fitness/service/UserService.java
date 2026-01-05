package com.project.fitness.service;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.RegisteredRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisteredRequest registeredRequest) {
        User user = modelMapper.map(registeredRequest,User.class);
        user.setPassword(passwordEncoder.encode(registeredRequest.getPassword()));
        userRepository.save(user);
        return modelMapper.map(user,UserResponse.class);
    }

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user==null)
        {
            throw new RuntimeException("Invalid Credentials");
        }
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword()))
        {
            throw new RuntimeException("Invalid Credentials");
        }

        return user;
    }
}
