package com.subscriptiontracker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionFolderDTO {
    private Long id;
    private String name;
    private List<SubscriptionDTO> subscriptions;
}