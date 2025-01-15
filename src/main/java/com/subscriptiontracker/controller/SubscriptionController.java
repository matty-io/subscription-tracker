package com.subscriptiontracker.controller;

import com.subscriptiontracker.enums.BillingCycle;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import com.subscriptiontracker.service.SubscriptionFolderService;
import com.subscriptiontracker.service.SubscriptionService;
import com.subscriptiontracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService service;
    private final SubscriptionFolderService folderService;

    @PostMapping
    public ResponseEntity<Subscription> create(@RequestBody Subscription subscription) {
        return ResponseEntity.ok(service.createSubscription(subscription));
    }
}
