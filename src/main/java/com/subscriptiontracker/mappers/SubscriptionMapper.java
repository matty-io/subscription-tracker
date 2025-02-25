package com.subscriptiontracker.mappers;

import com.subscriptiontracker.DTO.*;
import com.subscriptiontracker.model.*;
import com.subscriptiontracker.repository.CompanyRepository;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import com.subscriptiontracker.repository.UserRepository;
import com.subscriptiontracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SubscriptionMapper {

    private final CompanyRepository companyRepository;

    private final SubscriptionFolderRepository folderRepository;

    private final UserRepository userRepository;

    private final SubscriptionRepository subscriptionRepository;

    public Subscription toEntity(SubscriptionRequest request) {
        Subscription subscription = findOrCreateSubscription(request);

        subscription.setCompany(getOrCreateCompany(request));
        subscription.setDescription(request.getDescription());
        subscription.setPrice(request.getPrice());
        subscription.setType(request.getType());
        subscription.setRecurring(request.isRecurring());
        subscription.setBillingCycle(request.getBillingCycle());
        subscription.setNextBillingDate(request.getNextBillingDate());
        subscription.setContractExpiry(request.getContractExpiry());

        // Convert currency string to Currency object
        if (request.getCurrency() != null) {
            subscription.setCurrency(Currency.getInstance(request.getCurrency().toUpperCase()));
        } else {
            subscription.setCurrency(Currency.getInstance("USD")); // Default currency
        }

        if (request.getFolderId() != null) {
            folderRepository.findById(request.getFolderId()).ifPresent(subscription::setFolder);
        }

        // Remove old alerts and add new ones
        subscription.getAlerts().forEach(alert -> alert.setSubscription(null));
        subscription.getAlerts().clear();
        Optional.ofNullable(request.getAlerts())
                .ifPresent(alertRequests -> {
                    List<Alert> alerts = alertRequests.stream().map(alertRequest -> {
                        Alert alert = new Alert();
                        alert.setSubscription(subscription);
                        alert.setEmail(alertRequest.getEmail());
                        alert.setDaysBeforeBilling(alertRequest.getDaysBeforeBilling());
                        return alert;
                    }).toList();
                    subscription.getAlerts().addAll(alerts);
                });

        User user = SecurityUtil.getAuthenticatedUser();
        subscription.setUser(user);
        return subscription;
    }

    private Company getOrCreateCompany(SubscriptionRequest request) {
        Company company = null;
        if (request.getCompanyId() != null) {
            company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
        } else if (request.getCompanyName() != null && !request.getCompanyName().trim().isEmpty()) {
            company = new Company();
            company.setName(request.getCompanyName().trim());
            company = companyRepository.save(company);
        } else {
            throw new IllegalArgumentException("Either companyId or companyName must be provided");
        }
        return company;
    }

    // Convert Subscription to DTO
    public SubscriptionResponse convertToSubscriptionResponse(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setCompanyName(Optional.ofNullable(subscription.getCompany()).map(Company::getName).orElse(null));
        response.setCompanyId(Optional.ofNullable(subscription.getCompany()).map(Company::getId).orElse(null));
        response.setDescription(subscription.getDescription());
        response.setPrice(subscription.getPrice());
        response.setCurrency(Optional.ofNullable(subscription.getCurrency()).map(Currency::getCurrencyCode).orElse("USD"));
        response.setType(subscription.getType());
        response.setRecurring(subscription.isRecurring());
        response.setBillingCycle(subscription.getBillingCycle());
        response.setNextBillingDate(subscription.getNextBillingDate());
        response.setContractExpiry(subscription.getContractExpiry());
        response.setFolderId(Optional.ofNullable(subscription.getFolder()).map(SubscriptionFolder::getId).orElse(null));
        response.setUserId(subscription.getUser().getId());
        response.setAlerts(Optional.ofNullable(subscription.getAlerts()).map(alerts -> alerts.stream().map(this::mapToAlertRequest).toList()).orElse(Collections.emptyList()));
        return response;
    }

    public SubscriptionFolderResponse convertToFolderResponse(SubscriptionFolder folder) {
        SubscriptionFolderResponse dto = new SubscriptionFolderResponse();
        dto.setId(folder.getId());
        dto.setName(folder.getName());
        dto.setSubscriptions(folder.getSubscriptions().stream()
                .map(this::convertToSubscriptionResponse)
                .collect(Collectors.toList()));
        return dto;
    }

    private Subscription findOrCreateSubscription(SubscriptionRequest request) {
        if (request instanceof UpdateSubscriptionRequest updateRequest) {
            if (updateRequest.getId() == null)
                throw new IllegalArgumentException("Subscription ID is required for update");

            return subscriptionRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        }
        return new Subscription();
    }

    private AlertRequest mapToAlertRequest(Alert alert) {
        AlertRequest alertRequest = new AlertRequest();
        alertRequest.setEmail(alert.getEmail());
        alertRequest.setDaysBeforeBilling(alert.getDaysBeforeBilling());
        return alertRequest;
    }

}