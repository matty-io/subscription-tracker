package com.subscriptiontracker.service;

import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final SubscriptionFolderRepository subscriptionFolderRepository;

    public Subscription createSubscription(Subscription subscription) {
        return repository.save(subscription);
    }
}
