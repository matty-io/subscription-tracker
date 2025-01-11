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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class SubscriptionController {
    private final SubscriptionService service;
    private final SubscriptionFolderService folderService;

    @GetMapping("/{userId}")
    public List<SubscriptionFolder> getSubscriptions(@PathVariable Long userId) {
        return folderService.getAllSubscriptionFolders(userId);
    }
}
