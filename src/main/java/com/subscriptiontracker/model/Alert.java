package com.subscriptiontracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Embedded
    private AlertConfiguration configuration;

    private boolean isNotified = false;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @ManyToOne
    private Contact contact;
}
