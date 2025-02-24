package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.SubscriptionFolderResponse;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.service.SubscriptionFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription_folders")
public class SubscriptionFolderController {
    private final SubscriptionFolderService service;

    @PostMapping
    public ResponseEntity<SubscriptionFolder> create(@RequestBody SubscriptionFolder folder) {
       return ResponseEntity.ok(service.createFolder(folder));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionFolderResponse>> getAllFolders() {
        return ResponseEntity.ok(service.getAllSubscriptionFolders());
    }
}
