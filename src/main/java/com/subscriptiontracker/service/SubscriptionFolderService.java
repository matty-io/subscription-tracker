package com.subscriptiontracker.service;

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

    public List<SubscriptionFolder> getAllSubscriptionFolders() {
        User user = SecurityUtil.getAuthenticatedUser();
        List<SubscriptionFolder> folders =  repository.findAllByUserId(user.getId()).orElse(new ArrayList<>());
        SubscriptionFolder allFolder = new SubscriptionFolder((long) -1, "All", user, new ArrayList<>());
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());
        allFolder.setSubscriptions(subscriptions);
        folders.addFirst(allFolder);
        return folders;
    }
}
