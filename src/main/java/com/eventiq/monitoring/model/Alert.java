package com.eventiq.monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String projectId;

    private String userId;

    private Long monitoringId;

    private String name;

    private String alertType;

    private String alertThreshold;

    private String notificationMethod;

    private String notificationValue;

    private Boolean isEnabled;
}
