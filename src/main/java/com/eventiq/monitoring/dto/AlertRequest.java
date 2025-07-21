package com.eventiq.monitoring.dto;

import lombok.Data;

@Data
public class AlertRequest {

    private String projectId;

    private Long monitoringId;

    private String name;

    private String alertType;

    private String alertThreshold;

    private String notificationMethod;

    private String notificationValue;
}
