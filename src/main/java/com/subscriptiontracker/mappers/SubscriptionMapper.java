package com.subscriptiontracker.mappers;

import com.subscriptiontracker.DTO.*;
import com.subscriptiontracker.model.*;
import com.subscriptiontracker.repository.*;
import com.subscriptiontracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SubscriptionMapper {

    private final CompanyRepository companyRepository;

    private final SubscriptionFolderRepository folderRepository;

    private final ContactRepository contactRepository;

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
        subscription.setCurrency(getCurrency(request.getCurrency()));
        subscription.setFolder(getFolder(request.getFolderId()));

        updateAlerts(subscription, request.getAlerts());

        subscription.setUser(SecurityUtil.getAuthenticatedUser());
        return subscription;
    }

    /**
     * Converts currency string to Currency object, defaults to USD if null.
     */
    private Currency getCurrency(String currencyCode) {
        return Currency.getInstance(Optional.ofNullable(currencyCode)
                .map(String::toUpperCase)
                .orElse("USD"));
    }

    /**
     * Retrieves a folder if provided.
     */
    private SubscriptionFolder getFolder(Long folderId) {
        return Optional.ofNullable(folderId)
                .flatMap(folderRepository::findById)
                .orElse(null);
    }

    /**
     * Updates alerts based on the request.
     */
    private void updateAlerts(Subscription subscription, List<AlertRequest> alertRequests) {
        Map<Long, Alert> existingAlerts = subscription.getAlerts().stream()
                .collect(Collectors.toMap(Alert::getId, alert -> alert));

        // Remove alerts not present in request
        subscription.getAlerts().removeIf(alert ->
                alertRequests.stream().noneMatch(a -> a.getId() != null && a.getId().equals(alert.getId())));

        alertRequests.forEach(alertRequest -> {
            Alert alert = existingAlerts.getOrDefault(alertRequest.getId(), new Alert());
            alert.setSubscription(subscription);
            alert.setContact(getContact(alertRequest.getContactId()));
            alert.setConfiguration(createAlertConfiguration(alertRequest));

            subscription.getAlerts().add(alert); // Ensures alert is added without redundant checks
        });
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
        alertRequest.setId(alert.getId());
        alertRequest.setEmail(alert.getContact()!=null ? alert.getContact().getEmail() : null);
        return alertRequest;
    }

    /**
     * Retrieves the contact based on ID or throws an exception if not found.
     */
    private Contact getContact(Long contactId) {
        return contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));
    }

    /**
     * Creates an AlertConfiguration from an AlertRequest.
     */
    private AlertConfiguration createAlertConfiguration(AlertRequest alertRequest) {
        return new AlertConfiguration(
                alertRequest.getReminderPeriod(),
                alertRequest.getTimeUnit(),
                alertRequest.getTriggerType()
        );
    }

}