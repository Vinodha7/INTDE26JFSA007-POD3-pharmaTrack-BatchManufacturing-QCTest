package com.cts.pharmaTrack.module
    .batchManufacturing.controller;

import com.cts.pharmaTrack.module
    .batchManufacturing.entity.BatchRecord;
import com.cts.pharmaTrack.module
    .batchManufacturing.service.BatchRecordService;
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
public class BatchRecordController {
    private static final Logger logger = LoggerFactory.getLogger(BatchRecordController.class);

    private final BatchRecordService service;

    public BatchRecordController(BatchRecordService service) {
        this.service = service;
    }

    @PostMapping("/createBatch")
    public ResponseEntity<?> createBatch(
            @RequestBody BatchRecord batch) {
        logger.info("POST /createBatch request received with batchNumber: {}", batch.getBatchNumber());
        try {
            Map<String, String> response =
                service.createBatch(batch);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains(
                    "already exists")) {
                return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message",
                        e.getMessage()));
            }
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message",
                    e.getMessage()));
        }
    }

    @GetMapping("/retrieveBatches")
    public ResponseEntity<?> retrieveBatches() {
        logger.info("GET /retrieveBatches request received");
        try {
            List<BatchRecord> batches =
                service.retrieveBatches();
            if (batches.isEmpty()) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message",
                        "No batches found"));
            }
            return ResponseEntity.ok(batches);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus
                    .INTERNAL_SERVER_ERROR)
                .body(Map.of("message",
                    e.getMessage()));
        }
    }

    @GetMapping("/retrieveBatchById/{batch_id}")
    public ResponseEntity<?> retrieveBatchById(
            @PathVariable("batch_id") int batchId) {
        logger.info("GET /retrieveBatchById/{} request received with batchId: {}", batchId, batchId);
        try {
            BatchRecord batch =
                service.retrieveBatchById(batchId);
            return ResponseEntity.ok(batch);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "Batch not found"));
        }
    }

    @PutMapping("/updateBatch/{batch_id}")
    public ResponseEntity<?> updateBatch(
            @PathVariable("batch_id") int batchId,
            @RequestBody BatchRecord batch) {
        logger.info("PUT /updateBatch/{} request received with batchId: {}", batchId, batchId);
        try {
            Map<String, String> response =
                service.updateBatch(batchId, batch);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "Batch not found"));
        }
    }

    @PutMapping("/updateBatchStatus/{batch_id}")
    public ResponseEntity<?> updateBatchStatus(
            @PathVariable("batch_id") int batchId,
            @RequestBody Map<String, String> body) {
        logger.info("PUT /updateBatchStatus/{} request received with batchId: {}", batchId, batchId);
        try {
            String newStatus = body.get("status");
            Map<String, String> response =
                service.updateBatchStatus(
                    batchId, newStatus);
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