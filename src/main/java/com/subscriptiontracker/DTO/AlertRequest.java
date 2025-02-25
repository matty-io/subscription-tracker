package com.subscriptiontracker.DTO;

import lombok.Data;

@Data
public class AlertRequest {
    private String email;
    private int daysBeforeBilling;
}