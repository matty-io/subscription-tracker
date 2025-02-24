package com.subscriptiontracker.mappers;

import com.subscriptiontracker.DTO.*;
import com.subscriptiontracker.model.Company;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.CompanyRepository;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import com.subscriptiontracker.repository.UserRepository;
import com.subscriptiontracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Currency;
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
        subscription.setCompany(company);

        // Map simple fields
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

        User user = SecurityUtil.getAuthenticatedUser();
        subscription.setUser(user);
        return subscription;
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
}