package com.subscriptiontracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.subscriptiontracker.enums.BillingCycle;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private Double price;

    @Enumerated(EnumType.STRING)
    private BillingCycle billingCycle;

    private Date nextBillingDate;

    @ManyToOne
    @JoinColumn(name = "folder_id")
    @JsonIgnore
    private SubscriptionFolder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
