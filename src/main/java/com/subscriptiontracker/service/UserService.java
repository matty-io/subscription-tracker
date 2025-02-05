package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.CreateUserRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.exception.ApiException;
import com.subscriptiontracker.jobs.SendWelcomeEmailJob;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.model.VerificationCode;
import com.subscriptiontracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.BackgroundJobRequest;
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
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new BadCredentialsException("Cannot find user with email " + email));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        User user  = new User(request);
        user = userRepository.save(user);
        sendVerificationEmail(user);
        return new UserResponse(user);
    }

    private void sendVerificationEmail(User user) {
        VerificationCode verificationCode = new VerificationCode(user);
        user.setVerificationCode(verificationCode);
        verificationCodeRepository.save(verificationCode);
        SendWelcomeEmailJob sendWelcomEmailJob = new SendWelcomeEmailJob(user.getId());
        BackgroundJobRequest.enqueue(sendWelcomEmailJob);
    }

    public User findById(Long id) {
        return  userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Transactional
    public void verifyEmail(String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
                .orElseThrow(() -> ApiException.builder().status(400).message("Invalid token").build());
        User user = verificationCode.getUser();
        user.setVerified(true);
        user.setVerificationCode(null);
        userRepository.save(user);
        verificationCodeRepository.delete(verificationCode);
    }
}
