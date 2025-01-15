package com.subscriptiontracker.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BillingCycle {
    MONTHLY("monthly"), YEARLY("yearly");

    private final String value;


    BillingCycle(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
