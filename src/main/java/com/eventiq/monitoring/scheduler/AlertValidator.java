package com.eventiq.monitoring.scheduler;

import com.eventiq.monitoring.model.Alert;
import com.eventiq.monitoring.model.Monitoring;
import com.eventiq.monitoring.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class AlertValidator {

    RestTemplate restTemplate;

    RedisTemplate<String, Object> redisTemplate;

    AlertExecutor alertExecutor;

    @Autowired
    public AlertValidator(RestTemplate restTemplate, RedisTemplate<String, Object> redisTemplate, AlertExecutor alertExecutor){
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.alertExecutor = alertExecutor;
    }

    public void validate(Alert alert, Monitoring probe) {
        System.out.println("Validator");
        String probeKey = probe.getProjectId() + probe.getId() + Constants.MONITORING;

        try {
            long start = System.currentTimeMillis();
            System.out.println(probe.getUrl());
            ResponseEntity<String> response = restTemplate.exchange(probe.getUrl(), HttpMethod.GET, null, String.class);
            long latency = System.currentTimeMillis() - start;

            HttpStatusCode statusCode = response.getStatusCode();

            redisTemplate.opsForHash().put(probeKey, Constants.LATENCY, latency);
            redisTemplate.opsForHash().put(probeKey, Constants.STATUS, statusCode.is2xxSuccessful() ? "UP" : "DOWN");
            redisTemplate.opsForHash().put(probeKey, Constants.LAST_CHECKED, LocalDateTime.now().toString());

            switch (alert.getAlertType()) {
                case Constants.ALERT_TYPE_DOWNTIME:
                    if (!statusCode.is2xxSuccessful()) {
                        alertExecutor.executeAlert(alert);
                    }
                    break;

                case Constants.ALERT_TYPE_RESPONSE_TIME:
                    if (latency > Long.parseLong(alert.getAlertThreshold())) {
                        alertExecutor.executeAlert(alert);
                    }
                    break;

                case Constants.ALERT_TYPE_STATUSCODE:
                    if (statusCode.value() == Integer.parseInt(alert.getAlertThreshold())) {
                        alertExecutor.executeAlert(alert);
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redisTemplate.opsForHash().put(probeKey, Constants.STATUS, "UNREACHABLE");
            if (Constants.ALERT_TYPE_DOWNTIME.equals(alert.getAlertType())) {
                alertExecutor.executeAlert(alert);
            }
        }
    }
}
