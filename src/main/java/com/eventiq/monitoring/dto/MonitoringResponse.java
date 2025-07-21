package com.eventiq.monitoring.dto;

import lombok.Data;

@Data
public class MonitoringResponse {

    private Long id;

    private String projectId;

    private String name;

    private String type;

    private String url;

    private Integer checkInterval;

    private Boolean enabled;

    private String status;

    private Integer responseTime;
}
