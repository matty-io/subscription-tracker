package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.AlertRequest;
import com.subscriptiontracker.DTO.AlertResponse;
import com.subscriptiontracker.model.Alert;
import com.subscriptiontracker.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alerts")
public class AlertController {
    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertResponse> createAlert(@RequestBody AlertRequest request) {
        Alert alert = alertService.createAlert(request);
        return ResponseEntity.ok(new AlertResponse(alert));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertResponse> updateAlert(@PathVariable Long id, @RequestBody AlertRequest request) {
        Alert alert = alertService.updateAlert(id, request);
        return ResponseEntity.ok(new AlertResponse(alert));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponse> getAlert(@PathVariable Long id) {
        Alert alert = alertService.getAlertById(id);
        return ResponseEntity.ok(new AlertResponse(alert));
    }

    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<List<AlertResponse>> getAlertsBySubscription(@PathVariable Long subscriptionId) {
        List<Alert> alerts = alertService.getAlertsBySubscription(subscriptionId);
        List<AlertResponse> responses = alerts.stream()
                .map(AlertResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AlertResponse>> getPendingAlerts() {
        List<Alert> alerts = alertService.getAllPendingAlerts();
        List<AlertResponse> responses = alerts.stream()
                .map(AlertResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}