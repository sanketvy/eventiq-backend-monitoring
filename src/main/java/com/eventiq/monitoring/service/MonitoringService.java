package com.eventiq.monitoring.service;

import com.eventiq.monitoring.dto.MonitoringRequest;
import com.eventiq.monitoring.dto.MonitoringResponse;
import com.eventiq.monitoring.model.Monitoring;
import com.eventiq.monitoring.repository.MonitoringRepository;
import com.eventiq.monitoring.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MonitoringService {

    MonitoringRepository monitoringRepository;

    RedisTemplate<String, Object> redisTemplate;

    public MonitoringService(MonitoringRepository monitoringRepository, RedisTemplate<String, Object> redisTemplate){
        this.monitoringRepository = monitoringRepository;
        this.redisTemplate = redisTemplate;
    }

    public void createProbe(Jwt token, MonitoringRequest monitoringRequest) {
        Monitoring monitoring = new Monitoring();
        monitoring.setUserId(token.getClaimAsString("sub"));
        monitoring.setProjectId(monitoringRequest.getProjectId());
        monitoring.setInterval(monitoringRequest.getInterval());
        monitoring.setEnabled(true);
        monitoring.setName(monitoringRequest.getName());
        monitoring.setType(monitoringRequest.getType());
        monitoring.setUrl(monitoringRequest.getUrl());
        monitoringRepository.save(monitoring);
    }

    public List<MonitoringResponse> getProbesByUserId(String sub, String projectId) {
        List<MonitoringResponse> response = new ArrayList<>();

        List<Monitoring> data = monitoringRepository.findAllByUserIdAndProjectId(sub, projectId);

        if(!data.isEmpty()){
            data.forEach(monitoring -> {
                MonitoringResponse monitoringResponse = new MonitoringResponse();

                monitoringResponse.setName(monitoring.getName());
                monitoringResponse.setId(monitoring.getId());
                monitoringResponse.setType(monitoring.getType());
                monitoringResponse.setCheckInterval(monitoring.getInterval());
                monitoringResponse.setEnabled(monitoring.getEnabled());
                monitoringResponse.setUrl(monitoring.getUrl());
                monitoringResponse.setResponseTime((Integer) Optional.ofNullable(redisTemplate.opsForHash().get(monitoring.getProjectId() + monitoring.getId() + Constants.MONITORING, Constants.LATENCY)).orElse(0));
                monitoringResponse.setStatus((String) Optional.ofNullable(redisTemplate.opsForHash().get(monitoring.getProjectId() + monitoring.getId() + Constants.MONITORING, Constants.STATUS)).orElse("UNKNOWN"));

                String lastCheckedStr = (String) redisTemplate.opsForHash().get(
                        monitoring.getProjectId() + monitoring.getId() + Constants.MONITORING,
                        Constants.LAST_CHECKED
                );
                if (lastCheckedStr != null) {
                    LocalDateTime lastChecked = LocalDateTime.parse(lastCheckedStr); // Ensure the format matches
                    Duration duration = Duration.between(lastChecked, LocalDateTime.now());
                    monitoringResponse.setLastChecked((int) duration.getSeconds());
                }
                response.add(monitoringResponse);
            });
        }
        return response;
    }

    public void deleteProbe(String monitoringId) {
        monitoringRepository.deleteById(Long.valueOf(monitoringId));
    }
}
