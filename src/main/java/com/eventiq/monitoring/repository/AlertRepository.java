package com.eventiq.monitoring.repository;

import com.eventiq.monitoring.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByUserId(String userId);

    List<Alert> findAllByMonitoringId(Long monitoringId);
}
