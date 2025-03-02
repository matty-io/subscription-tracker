package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.BillingCycle;
import com.subscriptiontracker.enums.SubscriptionType;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private String CompanyName;
    private Long companyId;
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