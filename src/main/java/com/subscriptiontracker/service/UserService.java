package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.CreateUserRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    final private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BadCredentialsException("Cannot find user with email " + email));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        User user  = new User(request);
        user = userRepository.save(user);
        return new UserResponse(user);
    }

    public User findById(Long id) {
        return  userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }
}
