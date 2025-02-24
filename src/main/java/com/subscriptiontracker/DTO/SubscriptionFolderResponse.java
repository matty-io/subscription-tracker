package com.subscriptiontracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionFolderResponse {
    private Long id;
    private String name;
    private List<SubscriptionResponse> subscriptions;
}