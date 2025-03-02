package com.subscriptiontracker.model;

import com.subscriptiontracker.enums.AlertTriggerType;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.temporal.ChronoUnit;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertConfiguration {
    private int reminderPeriod;
    private ChronoUnit timeUnit;
    private AlertTriggerType triggerType;
}