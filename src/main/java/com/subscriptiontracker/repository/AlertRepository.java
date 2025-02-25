package com.subscriptiontracker.repository;

import com.subscriptiontracker.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    // ✅ Find alerts for a specific subscription
    List<Alert> findBySubscriptionId(Long subscriptionId);

    // ✅ Find alerts that have not been notified yet
    List<Alert> findByIsNotifiedFalse();
}