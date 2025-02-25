package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.BillingCycle;
import com.subscriptiontracker.enums.SubscriptionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public abstract class SubscriptionRequest {
    private Long companyId;
    private String companyName;
    private String description;
    private Double price;
    private String currency;
    private SubscriptionType type;
    private boolean recurring;
    private BillingCycle billingCycle;
    private Date nextBillingDate;
    private Date contractExpiry;
    private Long folderId;
    private Long userId;
    private List<AlertRequest> alerts;
}