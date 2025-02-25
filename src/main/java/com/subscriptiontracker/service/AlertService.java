package com.subscriptiontracker.service;

import com.subscriptiontracker.email.EmailService;
import com.subscriptiontracker.model.Alert;
import com.subscriptiontracker.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final EmailService emailService;

    public void sendAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        emailService.sendSimpleEmail(List.of(alert.getEmail()), "Subscription Reminder",
                "Your subscription for " + alert.getSubscription().getCompany().getName() + 
                " is due on " + alert.getSubscription().getNextBillingDate());

        log.info("🚀 Executing alert job for {} (alert ID: {})", alert.getEmail(), alertId);

        alert.setNotified(true);
        alertRepository.save(alert); // ✅ Mark alert as notified
    }
}