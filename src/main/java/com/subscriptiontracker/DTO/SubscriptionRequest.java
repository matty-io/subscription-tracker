package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.BillingCycle;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SubscriptionRequest {
    private Long id;
    private String name;
    private Double price;
    private BillingCycle billingCycle;
    private Date nextBillingDate;
    private Long folderId;
}