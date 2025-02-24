package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.SubscriptionFolderResponse;
import com.subscriptiontracker.mappers.SubscriptionMapper;
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
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionFolder createFolder(SubscriptionFolder folder) {
        User user = SecurityUtil.getAuthenticatedUser();
        folder.setUser(user);
        return repository.save(folder);
    }

    public Optional<SubscriptionFolder> findFolderById(Long id) {
        return repository.findById(id);
    }

    public List<SubscriptionFolderResponse> getAllSubscriptionFolders() {
        User user = SecurityUtil.getAuthenticatedUser();
        List<SubscriptionFolder> folders = repository.findAllByUserId(user.getId()).orElse(new ArrayList<>());

        List<SubscriptionFolderResponse> folderDTOs = folders.stream().map(subscriptionMapper::convertToFolderResponse).collect(Collectors.toList());

        List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());
        SubscriptionFolderResponse allFolderDTO = new SubscriptionFolderResponse(
                -1L,
                "All",
                subscriptions.stream().map(subscriptionMapper::convertToSubscriptionResponse).collect(Collectors.toList())
        );

        folderDTOs.addFirst(allFolderDTO);
        return folderDTOs;
    }
}
