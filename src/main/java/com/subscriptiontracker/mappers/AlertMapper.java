package com.subscriptiontracker.mappers;

import com.subscriptiontracker.DTO.AlertRequest;
import com.subscriptiontracker.DTO.AlertResponse;
import com.subscriptiontracker.model.Alert;
import com.subscriptiontracker.model.AlertConfiguration;
import com.subscriptiontracker.model.Contact;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.repository.ContactRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AlertMapper {
    private final ContactRepository contactRepository;
    private final SubscriptionRepository subscriptionRepository;
    
    public Alert toEntity(AlertRequest request) {
        Alert alert = new Alert();
        
        // Set up the AlertConfiguration
        AlertConfiguration configuration = new AlertConfiguration(
                request.getReminderPeriod(),
                request.getTimeUnit(),
                request.getTriggerType());
        alert.setConfiguration(configuration);
        
        // Set contact if contactId is provided
        if (request.getContactId() != null) {
            Optional<Contact> contact = contactRepository.findById(request.getContactId());
            contact.ifPresent(alert::setContact);
        }
        
        // Set subscription if subscriptionId is provided
        if (request.getSubscriptionId() != null) {
            Optional<Subscription> subscription = subscriptionRepository.findById(request.getSubscriptionId());
            subscription.ifPresent(alert::setSubscription);
        }
        
        return alert;
    }
    
    public AlertResponse toResponse(Alert alert) {
        return new AlertResponse(alert);
    }
}