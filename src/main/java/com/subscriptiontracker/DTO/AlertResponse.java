package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.AlertTriggerType;
import com.subscriptiontracker.model.Alert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertResponse {
    private Long id;
    private String email;
    private int reminderPeriod;
    private ChronoUnit timeUnit;
    private AlertTriggerType triggerType;
    private Long contactId;
    private Long subscriptionId;
    private boolean notified;
    
    public AlertResponse(Alert alert) {
        this.id = alert.getId();
        this.email = alert.getContact() != null ? alert.getContact().getEmail() : null;
        this.reminderPeriod = alert.getConfiguration().getReminderPeriod();
        this.timeUnit = alert.getConfiguration().getTimeUnit();
        this.triggerType = alert.getConfiguration().getTriggerType();
        this.contactId = alert.getContact() != null ? alert.getContact().getId() : null;
        this.subscriptionId = alert.getSubscription() != null ? alert.getSubscription().getId() : null;
        this.notified = alert.isNotified();
    }
}