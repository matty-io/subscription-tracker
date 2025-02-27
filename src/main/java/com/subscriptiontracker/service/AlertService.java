package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.AlertRequest;
import com.subscriptiontracker.email.EmailService;
import com.subscriptiontracker.exception.ResourceNotFoundException;
import com.subscriptiontracker.jobs.helpers.BillingJobHandler;
import com.subscriptiontracker.model.Alert;
import com.subscriptiontracker.model.AlertConfiguration;
import com.subscriptiontracker.model.Contact;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.repository.AlertRepository;
import com.subscriptiontracker.repository.ContactRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository alertRepository;
    private final EmailService emailService;
    private final SubscriptionRepository subscriptionRepository;
    private final ContactRepository contactRepository;
    private final BillingJobHandler billingJobHandler;

    public void sendAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        emailService.sendSimpleEmail(List.of(alert.getContact().getEmail()), "Subscription Reminder",
                "Your subscription for " + alert.getSubscription().getCompany().getName() +
                        " is due on " + alert.getSubscription().getNextBillingDate());

        log.info("🚀 Executing alert job for {} (alert ID: {})", alert.getContact().getEmail(), alertId);

        alert.setNotified(true);
        alertRepository.save(alert); // ✅ Mark alert as notified
    }

    @Transactional
    public Alert createAlert(AlertRequest request) {
        Alert alert = new Alert();

        Subscription subscription = subscriptionRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with ID: " + request.getId()));

        Contact contact = null;
        if (request.getContactId() != null) {
            contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact not found with ID: " + request.getContactId()));
        } else if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            contact = new Contact();
            contact.setEmail(request.getEmail());
            contact = contactRepository.save(contact);
        } else {
            throw new IllegalArgumentException("Either contact ID or email must be provided");
        }

        AlertConfiguration configuration = new AlertConfiguration(
                request.getReminderPeriod(),
                request.getTimeUnit(),
                request.getTriggerType());

        alert.setConfiguration(configuration);
        alert.setSubscription(subscription);
        alert.setContact(contact);
        alert.setNotified(false);

        alert = alertRepository.save(alert);

        // Schedule the billing job for the new alert
        billingJobHandler.scheduleNextBillingJob(alert.getId());

        return alert;
    }

    @Transactional
    public Alert updateAlert(Long id, AlertRequest request) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID: " + id));

        Contact contact;
        if (request.getContactId() != null) {
            contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("Contact not found with ID: " + request.getContactId()));
            alert.setContact(contact);
        } else if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // If email changed, update or create new contact
            if (alert.getContact() == null || !alert.getContact().getEmail().equals(request.getEmail())) {
                contact = new Contact();
                contact.setEmail(request.getEmail());
                contact = contactRepository.save(contact);
                alert.setContact(contact);
            }
        }

        // Update the alert configuration
        AlertConfiguration configuration = new AlertConfiguration(
                request.getReminderPeriod(),
                request.getTimeUnit(),
                request.getTriggerType());

        alert.setConfiguration(configuration);

        // Reset notification status if configuration changed
        alert.setNotified(false);

        Alert updatedAlert = alertRepository.save(alert);

        // Reschedule the billing job with the updated configuration
        billingJobHandler.scheduleNextBillingJob(updatedAlert.getId());

        return updatedAlert;
    }

    public void deleteAlert(Long id) {
        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID: " + id));

        alertRepository.delete(alert);

        // Cancel the scheduled job for this alert
       // billingJobHandler.cancelJob(id);
    }

    public Alert getAlertById(Long id) {
        return alertRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found with ID: " + id));
    }

    public List<Alert> getAlertsBySubscription(Long subscriptionId) {
        return alertRepository.findBySubscriptionId(subscriptionId);
    }

    public List<Alert> getAllPendingAlerts() {
        return alertRepository.findByIsNotifiedFalse();
    }
}