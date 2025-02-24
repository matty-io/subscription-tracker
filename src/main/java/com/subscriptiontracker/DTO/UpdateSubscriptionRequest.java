package com.subscriptiontracker.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSubscriptionRequest extends SubscriptionRequest {
    @NotNull(message = "Subscription ID is required for update")
    private Long id;
}