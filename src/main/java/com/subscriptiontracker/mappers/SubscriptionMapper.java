package com.subscriptiontracker.mappers;

import com.subscriptiontracker.DTO.CreateSubscriptionRequest;
import com.subscriptiontracker.DTO.SubscriptionFolderResponse;
import com.subscriptiontracker.DTO.SubscriptionResponse;
import com.subscriptiontracker.model.Company;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.repository.CompanyRepository;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SubscriptionMapper {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private SubscriptionFolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    public Subscription toEntity(CreateSubscriptionRequest request) {
        Subscription subscription = new Subscription();

        // Handle company
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

        // Set folder if provided
        if (request.getFolderId() != null) {
            folderRepository.findById(request.getFolderId()).ifPresent(subscription::setFolder);
        }

        // Set user if provided
        if (request.getUserId() != null) {
            userRepository.findById(request.getUserId()).ifPresent(subscription::setUser);
        }

        return subscription;
    }

    // Convert Subscription to DTO
    public SubscriptionResponse convertToSubscriptionResponse(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setCompanyName(Optional.ofNullable(subscription.getCompany()).map(Company::getName).orElse(null));
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
}