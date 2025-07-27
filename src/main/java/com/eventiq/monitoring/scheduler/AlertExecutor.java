package com.eventiq.monitoring.scheduler;

import com.eventiq.monitoring.model.Alert;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AlertExecutor {

    public void executeAlert(Alert alert) {
        if (alert.getNotificationMethod().equals("EMAIL")) {
            Arrays.stream(alert.getNotificationValue().split(",")).forEach(email -> {
                email = email.trim();
                // send email
            });
        }
    }
}

