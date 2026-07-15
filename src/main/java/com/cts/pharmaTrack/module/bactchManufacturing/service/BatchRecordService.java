package com.cts.pharmaTrack.module
    .batchManufacturing.service;

import com.cts.pharmaTrack.module
    .batchManufacturing.entity.BatchRecord;
import com.cts.pharmaTrack.module
    .batchManufacturing.repository
    .BatchRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BatchRecordService {
    private static final Logger logger = LoggerFactory.getLogger(BatchRecordService.class);

    private final BatchRecordRepository repository;

    public BatchRecordService(BatchRecordRepository repository) {
        this.repository = repository;
    }

    private static final Map<String, List<String>>
        TRANSITIONS = new HashMap<>();
    static {
        TRANSITIONS.put("IP",  List.of("QCH"));
        TRANSITIONS.put("QCH", List.of("REL", "REJ"));
        TRANSITIONS.put("REL", List.of("RCL"));
        TRANSITIONS.put("REJ", List.of("DEL"));
        TRANSITIONS.put("RCL", List.of("DEL"));
    }

    public List<BatchRecord> retrieveBatches() {
        logger.info("Executing retrieveBatches");
        return repository.findAll();
    }

    public BatchRecord retrieveBatchById(int id) {
        logger.info("Executing retrieveBatchById with id: {}", id);
        return repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Batch not found"));
    }

    public Map<String, String> createBatch(
            BatchRecord batch) {
        logger.info("Executing createBatch with batchNumber: {}", batch.getBatchNumber());
        Optional<BatchRecord> existing =
            repository.findByBatchNumber(
                batch.getBatchNumber());
        if (existing.isPresent()) {
            throw new RuntimeException(
                "Batch number already exists");
        }
        batch.setStatus("IP");
        repository.save(batch);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Batch created successfully");
        return response;
    }

    public Map<String, String> updateBatch(
            int id, BatchRecord batch) {
        logger.info("Executing updateBatch with id: {}", id);
        BatchRecord existing =
            repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Batch not found"));
        existing.setProductId(batch.getProductId());
        existing.setBatchNumber(
            batch.getBatchNumber());
        existing.setManufacturingDate(
            batch.getManufacturingDate());
        existing.setExpiryDate(batch.getExpiryDate());
        existing.setQuantityManufactured(
            batch.getQuantityManufactured());
        existing.setUnit(batch.getUnit());
        existing.setManufacturingSiteId(
            batch.getManufacturingSiteId());
        repository.save(existing);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Batch updated successfully");
        return response;
    }

    public Map<String, String> updateBatchStatus(
            int id, String newStatus) {
        logger.info("Executing updateBatchStatus with id: {} and newStatus: {}", id, newStatus);
        BatchRecord existing =
            repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Batch not found"));
        String currentStatus = existing.getStatus();
        List<String> allowed =
            TRANSITIONS.getOrDefault(
                currentStatus, List.of());
        if (!allowed.contains(newStatus)) {
            throw new RuntimeException(
                "Status transition not allowed: "
                + currentStatus + " -> " + newStatus);
        }
        existing.setStatus(newStatus);
        repository.save(existing);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Status updated successfully");
        return response;
    }
}