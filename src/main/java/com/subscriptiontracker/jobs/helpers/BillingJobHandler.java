package com.subscriptiontracker.jobs.helpers;

import com.subscriptiontracker.jobs.SendBillingAlertJob;
import com.subscriptiontracker.model.Alert;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.repository.AlertRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.jobrunr.scheduling.BackgroundJobRequest;
import org.jobrunr.scheduling.JobScheduler;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingJobHandler {
    private final JobScheduler jobScheduler;
    private final AlertRepository alertRepository;
    private final SubscriptionRepository subscriptionRepository;

    public void scheduleNextBillingJob(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        Subscription subscription = alert.getSubscription();

        // ✅ Calculate next billing date
        LocalDate nextBillingDate = getNextBillingDate(subscription);
        Instant nextBillingInstant = nextBillingDate.atStartOfDay(ZoneId.systemDefault()).toInstant();


        UUID jobId = UUID.randomUUID();// UUID.nameUUIDFromBytes(("alert-" + alert.getId()).getBytes());

        //jobScheduler.delete(jobId);
        // ✅ Schedule recurring alert job
        BackgroundJobRequest.schedule(jobId, nextBillingInstant, new SendBillingAlertJob(alertId));
    }

    private LocalDate getNextBillingDate(Subscription subscription) {
        LocalDate currentBillingDate = subscription.getNextBillingDate()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return switch (subscription.getBillingCycle()) {
            case MONTHLY -> currentBillingDate.plusMonths(1);
            case YEARLY -> currentBillingDate.plusYears(1);
        };
    }
}