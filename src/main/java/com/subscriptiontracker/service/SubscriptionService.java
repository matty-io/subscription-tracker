package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.CreateSubscriptionRequest;
import com.subscriptiontracker.DTO.SubscriptionResponse;
import com.subscriptiontracker.DTO.UpdateSubscriptionRequest;
import com.subscriptiontracker.exception.ResourceNotFoundException;
import com.subscriptiontracker.jobs.helpers.BillingJobHandler;
import com.subscriptiontracker.mappers.SubscriptionMapper;
import com.subscriptiontracker.model.Alert;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import com.subscriptiontracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.states.StateName;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.storage.StorageProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository repository;
    private final SubscriptionMapper subscriptionMapper;
    private final BillingJobHandler billingJobHandler;

    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {
        Subscription subscription = subscriptionMapper.toEntity(request);
        Subscription savedSubscription = repository.save(subscription);
        savedSubscription.getAlerts().forEach(alert -> billingJobHandler.scheduleNextBillingJob(alert.getId()));
        return subscriptionMapper.convertToSubscriptionResponse(savedSubscription);
    }

    public SubscriptionResponse updateSubscription(UpdateSubscriptionRequest request) {
        Subscription subscription = repository.findById(request.getId()).orElse(new Subscription());
        subscriptionMapper.toEntity(request);
        Subscription savedSubscription = repository.save(subscription);
        savedSubscription.getAlerts().forEach(alert -> billingJobHandler.scheduleNextBillingJob(alert.getId()));
        return subscriptionMapper.convertToSubscriptionResponse(savedSubscription);
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
