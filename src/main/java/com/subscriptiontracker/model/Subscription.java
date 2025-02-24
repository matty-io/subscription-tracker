package com.subscriptiontracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subscriptiontracker.enums.BillingCycle;
import com.subscriptiontracker.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;
import java.util.Date;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private String description;

    private Double price;

    private Currency currency;

    private SubscriptionType type;

    private boolean recurring;

    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;

    private Date nextBillingDate;

    private Date contractExpiry;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private SubscriptionFolder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
