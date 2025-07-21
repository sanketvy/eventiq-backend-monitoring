package com.eventiq.monitoring.controller;

import com.eventiq.monitoring.dto.AlertRequest;
import com.eventiq.monitoring.dto.AlertResponse;
import com.eventiq.monitoring.service.AlertService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring/alert")
public class AlertController {

    AlertService alertService;

    public AlertController(AlertService alertService){
        this.alertService = alertService;
    }

    @GetMapping()
    public ResponseEntity<List<AlertResponse>> getAlerts(@AuthenticationPrincipal Jwt token){
        return ResponseEntity.status(HttpStatus.OK).body(alertService.getAlerts(token.getClaimAsString("sub")));
    }

    @PostMapping
    public ResponseEntity<String> createAlert(@AuthenticationPrincipal Jwt token, @RequestBody AlertRequest alertRequest){
        alertService.createAlert(token.getClaimAsString("sub"), alertRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ok");
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<String> deleteAlert(@AuthenticationPrincipal Jwt token, @PathVariable String alertId){
        alertService.deleteAlert(alertId);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
}
