package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.CreateUserRequest;
import com.subscriptiontracker.DTO.UserResponse;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.service.SubscriptionFolderService;
import com.subscriptiontracker.service.UserService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final SubscriptionFolderService folderService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.create(request));
    }

    @GetMapping("/{userId}/subscription-folders")
    public ResponseEntity<List<SubscriptionFolder>> getSubscriptionFolders(@PathVariable Long userId) {
        return ResponseEntity.ok(folderService.getAllSubscriptionFolders(userId));
    }
}
