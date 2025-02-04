package com.subscriptiontracker.DTO;

import com.subscriptiontracker.enums.BillingCycle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionDTO {
    private Long id;
    private String name;
    private Double price;
    private BillingCycle billingCycle;
    private Date nextBillingDate;
    private Long folderId;
}