package com.subscriptiontracker.repository;


import com.subscriptiontracker.model.SubscriptionFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionFolderRepository extends JpaRepository<SubscriptionFolder, Long> {
    @Query("SELECT sf from SubscriptionFolder sf where sf.user.id = :userId")
    Optional<List<SubscriptionFolder>> findAllByUserId(@Param("userId") Long userId);
}
