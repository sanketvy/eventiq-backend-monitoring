package com.eventiq.monitoring.repository;

import com.eventiq.monitoring.model.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
    List<Monitoring> findAllByUserId(String userId);

    List<Monitoring> findAllByUserIdAndProjectId(String userId, String projectId);
}
