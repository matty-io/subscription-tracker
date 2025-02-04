package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.SubscriptionRequest;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.service.SubscriptionFolderService;
import com.subscriptiontracker.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Subscription> create(@RequestBody Subscription subscription) {
        return ResponseEntity.ok(service.createSubscription(subscription));
    }

    @PutMapping
    public ResponseEntity<Subscription> updateSubscription(@RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(service.updateSubscription(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        service.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(service.getAllSubscriptions());
    }


}
