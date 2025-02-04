package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.SubscriptionDTO;
import com.subscriptiontracker.DTO.SubscriptionFolderDTO;
import com.subscriptiontracker.model.Subscription;
import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import com.subscriptiontracker.repository.SubscriptionRepository;
import com.subscriptiontracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionFolderService {
    private final SubscriptionFolderRepository repository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionFolder createFolder(SubscriptionFolder folder) {
        User user = SecurityUtil.getAuthenticatedUser();
        folder.setUser(user);
        return repository.save(folder);
    }

    public Optional<SubscriptionFolder> findFolderById(Long id) {
        return repository.findById(id);
    }

    public List<SubscriptionFolderDTO> getAllSubscriptionFolders() {
        User user = SecurityUtil.getAuthenticatedUser();
        List<SubscriptionFolder> folders = repository.findAllByUserId(user.getId()).orElse(new ArrayList<>());

        List<SubscriptionFolderDTO> folderDTOs = folders.stream().map(this::convertToDTO).collect(Collectors.toList());

        List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());
        SubscriptionFolderDTO allFolderDTO = new SubscriptionFolderDTO(
                -1L,
                "All",
                subscriptions.stream().map(this::convertToSubscriptionDTO).collect(Collectors.toList())
        );

        folderDTOs.addFirst(allFolderDTO);
        return folderDTOs;
    }

    private SubscriptionFolderDTO convertToDTO(SubscriptionFolder folder) {
        List<SubscriptionDTO> subscriptionDTOs = folder.getSubscriptions() != null
                ? folder.getSubscriptions().stream().map(this::convertToSubscriptionDTO).collect(Collectors.toList())
                : new ArrayList<>();

        return new SubscriptionFolderDTO(folder.getId(), folder.getName(), subscriptionDTOs);
    }

    private SubscriptionDTO convertToSubscriptionDTO(Subscription subscription) {
        return new SubscriptionDTO(
                subscription.getId(),
                subscription.getName(),
                subscription.getPrice(),
                subscription.getBillingCycle(),
                subscription.getNextBillingDate(),
                subscription.getFolder() != null ? subscription.getFolder().getId() : null
        );
    }
}
