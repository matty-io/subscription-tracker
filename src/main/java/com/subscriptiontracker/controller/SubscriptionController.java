package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.CreateSubscriptionRequest;
import com.subscriptiontracker.DTO.SubscriptionResponse;
import com.subscriptiontracker.DTO.UpdateSubscriptionRequest;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.service.SubscriptionFolderService;
import com.subscriptiontracker.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService service;
    private final SubscriptionFolderService folderService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@RequestBody CreateSubscriptionRequest request) {
        return ResponseEntity.ok(service.createSubscription(request));
    }

    @PutMapping
    public ResponseEntity<SubscriptionResponse> updateSubscription(@RequestBody UpdateSubscriptionRequest request) {
        return ResponseEntity.ok(service.updateSubscription(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable Long id) {
        return ResponseEntity.ok(service.getSubscription(id));
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() {
        return ResponseEntity.ok(service.getAllSubscriptions());
    }
}
