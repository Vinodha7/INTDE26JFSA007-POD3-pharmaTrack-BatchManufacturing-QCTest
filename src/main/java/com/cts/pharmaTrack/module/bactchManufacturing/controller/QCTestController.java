package com.cts.pharmaTrack.module
    .batchManufacturing.controller;

import com.cts.pharmaTrack.module
    .batchManufacturing.entity.QCTest;
import com.cts.pharmaTrack.module
    .batchManufacturing.service.QCTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pharmaTrack/batchManufacturing")
@CrossOrigin(origins = "http://localhost:4200")
public class QCTestController {
    private static final Logger logger = LoggerFactory.getLogger(QCTestController.class);

    private final QCTestService service;

    public QCTestController(QCTestService service) {
        this.service = service;
    }

    @PostMapping("/createQcTest")
    public ResponseEntity<?> createQCTest(
            @RequestBody QCTest test) {
        logger.info("POST /createQcTest request received with testType: {}", test.getTestType());
        try {
            Map<String, String> response =
                service.createQCTest(test);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message",
                    e.getMessage()));
        }
    }

    @GetMapping("/retrieveQcTests")
    public ResponseEntity<?> retrieveQCTests() {
        logger.info("GET /retrieveQcTests request received");
        try {
            List<QCTest> tests =
                service.retrieveQCTests();
            if (tests.isEmpty()) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message",
                        "No tests found"));
            }
            return ResponseEntity.ok(tests);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus
                    .INTERNAL_SERVER_ERROR)
                .body(Map.of("message",
                    e.getMessage()));
        }
    }

    @GetMapping("/retrieveQcTestById/{test_id}")
    public ResponseEntity<?> retrieveQCTestById(
            @PathVariable("test_id") int testId) {
        logger.info("GET /retrieveQcTestById/{} request received with testId: {}", testId, testId);
        try {
            QCTest test =
                service.retrieveQCTestById(testId);
            return ResponseEntity.ok(test);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "Test not found"));
        }
    }

    @GetMapping(
        "/retrieveQcTestByBatchId/{batch_id}")
    public ResponseEntity<?> retrieveQCTestByBatchId(
            @PathVariable("batch_id") int batchId) {
        logger.info("GET /retrieveQcTestByBatchId/{} request received with batchId: {}", batchId, batchId);
        try {
            List<QCTest> tests =
                service.retrieveQCTestByBatchId(
                    batchId);
            return ResponseEntity.ok(tests);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "No tests found"));
        }
    }

    @PutMapping("/updateQcTest/{test_id}")
    public ResponseEntity<?> updateQCTest(
            @PathVariable("test_id") int testId,
            @RequestBody QCTest test) {
        logger.info("PUT /updateQcTest/{} request received with testId: {}", testId, testId);
        try {
            Map<String, String> response =
                service.updateQCTest(testId, test);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "Test not found"));
        }
    }

    @PutMapping("/updateQcTestStatus/{test_id}")
    public ResponseEntity<?> updateQCTestStatus(
            @PathVariable("test_id") int testId,
            @RequestBody Map<String, String> body) {
        logger.info("PUT /updateQcTestStatus/{} request received with testId: {}", testId, testId);
        try {
            String newStatus = body.get("status");
            Map<String, String> response =
                service.updateQCTestStatus(
                    testId, newStatus);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains(
                    "not allowed")) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message",
                        e.getMessage()));
            }
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    e.getMessage()));
        }
    }
}