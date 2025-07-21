package com.eventiq.monitoring.dto;

import lombok.Data;

@Data
public class MonitoringRequest {

    private String projectId;

    private String name;

    private String type;

    private String url;

    private Integer interval;

    private Boolean enabled;
}
