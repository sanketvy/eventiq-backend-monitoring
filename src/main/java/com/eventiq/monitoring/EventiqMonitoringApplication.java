package com.eventiq.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class EventiqMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventiqMonitoringApplication.class, args);
    }

}
