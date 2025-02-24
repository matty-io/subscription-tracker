package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.BillingCycle;
import com.subscriptiontracker.enums.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UpdateSubscriptionRequest {
    @NotNull(message = "Subscription ID is required for update")
    private Long id;

    private String description;
    private Double price;
    private String currency;
    private SubscriptionType type;
    private boolean recurring;
    private BillingCycle billingCycle;
    private Date nextBillingDate;
    private Date contractExpiry;
    private Long folderId;
}