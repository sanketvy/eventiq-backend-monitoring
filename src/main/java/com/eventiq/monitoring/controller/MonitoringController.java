package com.eventiq.monitoring.controller;

import com.eventiq.monitoring.dto.MonitoringRequest;
import com.eventiq.monitoring.dto.MonitoringResponse;
import com.eventiq.monitoring.service.MonitoringService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring/probe")
public class MonitoringController {

    MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService){
        this.monitoringService = monitoringService;
    }

    @PostMapping
    public ResponseEntity<String> createMonitoring(@AuthenticationPrincipal Jwt token, @RequestBody MonitoringRequest monitoringRequest){
        monitoringService.createProbe(token, monitoringRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<MonitoringResponse>> getMonitoring(@AuthenticationPrincipal Jwt token, @PathVariable String projectId){
        return ResponseEntity.status(HttpStatus.OK).body(monitoringService.getProbesByUserId(token.getClaimAsString("sub"), projectId));
    }

    @DeleteMapping("/{monitoringId}")
    public ResponseEntity<String> deleteMonitoring(@AuthenticationPrincipal Jwt token, @PathVariable String monitoringId){
        monitoringService.deleteProbe(monitoringId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
