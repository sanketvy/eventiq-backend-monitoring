package com.eventiq.monitoring.scheduler;

import com.eventiq.monitoring.model.Alert;
import com.eventiq.monitoring.model.Monitoring;
import com.eventiq.monitoring.repository.AlertRepository;
import com.eventiq.monitoring.repository.MonitoringRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AlertsScheduler {

    RedisTemplate<String, Object> redisTemplate;

    AlertRepository alertRepository;

    MonitoringRepository monitoringRepository;

    ExecutorService executorService;

    AlertValidator alertValidator;

    public AlertsScheduler(RedisTemplate<String, Object> redisTemplate, AlertRepository alertRepository,MonitoringRepository monitoringRepository, ExecutorService executorService, AlertValidator alertValidator){
        this.redisTemplate = redisTemplate;
        this.alertRepository = alertRepository;
        this.monitoringRepository = monitoringRepository;
        this.executorService = executorService;
        this.alertValidator = alertValidator;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void validateAlerts(){
        boolean leader = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent("monitoring-leader", 0, 1, TimeUnit.MINUTES));
        if(leader){
            try {
                List<Monitoring> probesList = monitoringRepository.findAll();
                probesList.forEach(probe -> {
                    List<Alert> alerts = alertRepository.findAllByMonitoringId(probe.getId());
                    alerts.forEach(alert -> {
                        executorService.submit(() -> {
                            alertValidator.validate(alert, probe);
                        });
                    });
                });
            } finally {
                redisTemplate.delete("monitoring-leader");
            }
        }
    }
}
