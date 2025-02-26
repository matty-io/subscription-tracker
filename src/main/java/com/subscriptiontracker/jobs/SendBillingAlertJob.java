package com.subscriptiontracker.jobs;

import com.subscriptiontracker.jobs.handlers.SendBillingAlertJobHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.lambdas.JobRequest;
import org.jobrunr.jobs.lambdas.JobRequestHandler;

@Getter
@RequiredArgsConstructor
public class SendBillingAlertJob implements JobRequest {
    private final Long alertId;

    @Override
    public Class<? extends JobRequestHandler<SendBillingAlertJob>> getJobRequestHandler() {
        return SendBillingAlertJobHandler.class;
    }
}