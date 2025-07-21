package com.eventiq.monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Monitoring {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String projectId;

    private String userId;

    private String name;

    private String type;

    private String url;

    private Integer interval;

    private Boolean enabled;
}
