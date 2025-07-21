package com.eventiq.monitoring.service;

import com.eventiq.monitoring.dto.AlertRequest;
import com.eventiq.monitoring.dto.AlertResponse;
import com.eventiq.monitoring.model.Alert;
import com.eventiq.monitoring.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertService {
    AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository){
        this.alertRepository = alertRepository;
    }

    public List<AlertResponse> getAlerts(String sub) {
        List<AlertResponse> response = new ArrayList<>();

        List<Alert> alerts = alertRepository.findAllByUserId(sub);
        if(!alerts.isEmpty()){
            alerts.forEach(alert -> {
                AlertResponse alertResponse = new AlertResponse();
                alertResponse.setName(alert.getName());
                alertResponse.setId(alert.getId());
                alertResponse.setMonitoringId(alert.getMonitoringId());
                alertResponse.setIsEnabled(alert.getIsEnabled());
                alertResponse.setNotificationMethod(alert.getNotificationMethod());
                alertResponse.setNotificationValue(alert.getNotificationValue());
                alertResponse.setAlertType(alert.getAlertType());
                alertResponse.setAlertThreshold(alert.getAlertThreshold());
                response.add(alertResponse);
            });
        }
        return response;
    }

    public void createAlert(String sub, AlertRequest alertRequest) {
        Alert alert = new Alert();
        alert.setIsEnabled(true);
        alert.setMonitoringId(alertRequest.getMonitoringId());
        alert.setProjectId(alertRequest.getProjectId());
        alert.setUserId(sub);
        alert.setNotificationMethod(alertRequest.getNotificationMethod());
        alert.setName(alertRequest.getName());
        alert.setNotificationValue(alertRequest.getNotificationValue());
        alert.setAlertType(alertRequest.getAlertType());
        alert.setAlertThreshold(alertRequest.getAlertThreshold());
        alertRepository.save(alert);
    }

    public void deleteAlert(String alertId) {
        alertRepository.deleteById(Long.valueOf(alertId));
    }
}
