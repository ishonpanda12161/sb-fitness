package com.project.fitness.controller;

import com.project.fitness.dto.LoginRequest;
import com.project.fitness.dto.LoginResponse;
import com.project.fitness.dto.RegisteredRequest;
import com.project.fitness.dto.UserResponse;
import com.project.fitness.model.User;
import com.project.fitness.repository.UserRepository;
import com.project.fitness.security.JwtUtils;
import com.project.fitness.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> getUsers(@Valid @RequestBody RegisteredRequest registeredRequest)
    {
        UserResponse userResponse = userService.register(registeredRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest)
    {
        try{
            User user = userService.authenticate(loginRequest);
            String token = jwtUtils.generateToken(user.getId(),user.getRole().name());
            return ResponseEntity.ok(new LoginResponse(token,modelMapper.map(user,UserResponse.class)));

        }catch (AuthenticationException e)
        {
            return ResponseEntity.status(401).build();
        }
    }


}
