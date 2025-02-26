package com.subscriptiontracker.jobs.handlers;

import com.subscriptiontracker.jobs.SendBillingAlertJob;
import com.subscriptiontracker.jobs.helpers.BillingJobHandler;
import com.subscriptiontracker.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.lambdas.JobRequestHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendBillingAlertJobHandler implements JobRequestHandler<SendBillingAlertJob> {
    private final AlertService alertService;
    private final BillingJobHandler billingJobHandler;

    @Override
    public void run(SendBillingAlertJob job) {
        alertService.sendAlert(job.getAlertId());
        billingJobHandler.scheduleNextBillingJob(job.getAlertId());
    }
}