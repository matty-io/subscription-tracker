package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.AlertTriggerType;
import lombok.Data;

import java.time.temporal.ChronoUnit;

@Data
public class AlertRequest {
    private Long id;
    private int reminderPeriod;
    private ChronoUnit timeUnit;
    private AlertTriggerType triggerType;
    private Long contactId;
    private Long subscriptionId;
}