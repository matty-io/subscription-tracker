package com.subscriptiontracker.repository;

import com.subscriptiontracker.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findBySubscriptionId(Long subscriptionId);

    List<Alert> findByIsNotifiedFalse();
}