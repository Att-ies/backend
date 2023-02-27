package com.sptp.backend.common.controller;

import com.sptp.backend.aop.annotation.NoLogging;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusCheckController {

    @GetMapping("/health-check")
    @NoLogging
    public ResponseEntity<Void> checkHealthStatus() {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
