package com.subscriptiontracker.service;

import com.subscriptiontracker.exception.ResourceNotFoundException;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import com.subscriptiontracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final SubscriptionFolderRepository subscriptionFolderRepository;

    public Subscription createSubscription(Subscription subscription) {
        User user = SecurityUtil.getAuthenticatedUser();
        subscription.setUser(user);
        return repository.save(subscription);
    }

    public Subscription updateSubscription(Subscription subscription) {
        User user = SecurityUtil.getAuthenticatedUser();
        subscription.setUser(user);
        return repository.save(subscription);
    }

    public void deleteSubscription(Long id)  {
        User user = SecurityUtil.getAuthenticatedUser();
        Subscription subscription = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with ID: " + id));

        if (!user.getId().equals(subscription.getUser().getId()))
            throw  new AccessDeniedException("You do not have permission to delete this subscription.");

        repository.deleteById(id);
    }

    public List<Subscription> getAllSubscriptions() {
        User user = SecurityUtil.getAuthenticatedUser();
        return repository.findByUserId(user.getId());
    }
}
