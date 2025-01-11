package com.subscriptiontracker.service;

import com.subscriptiontracker.model.SubscriptionFolder;
import com.subscriptiontracker.repository.SubscriptionFolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionFolderService {
    private final SubscriptionFolderRepository repository;

    public SubscriptionFolder createFolder(SubscriptionFolder folder) {
        return repository.save(folder);
    }

    public Optional<SubscriptionFolder> findFolderById(Long id) {
        return repository.findById(id);
    }

    public List<SubscriptionFolder> getAllSubscriptionFolders(Long userId) {
        List<SubscriptionFolder> folders = repository.findAllByUserId(userId).orElse(new ArrayList<>());
        return folders;
    }
}
