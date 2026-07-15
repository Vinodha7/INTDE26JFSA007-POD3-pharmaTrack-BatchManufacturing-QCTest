package com.cts.pharmaTrack.module
    .batchManufacturing.service;

import com.cts.pharmaTrack.module
    .batchManufacturing.entity.QCTest;
import com.cts.pharmaTrack.module
    .batchManufacturing.repository.QCTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QCTestService {
    private static final Logger logger = LoggerFactory.getLogger(QCTestService.class);

    private final QCTestRepository repository;

    public QCTestService(QCTestRepository repository) {
        this.repository = repository;
    }

    private static final Map<String, List<String>>
        TRANSITIONS = new HashMap<>();
    static {
        TRANSITIONS.put("RT", List.of("P", "F"));
        TRANSITIONS.put("F",  List.of("RT", "DEL"));
        TRANSITIONS.put("P",  List.of("DEL"));
    }

    public List<QCTest> retrieveQCTests() {
        logger.info("Executing retrieveQCTests");
        return repository.findAll();
    }

    public QCTest retrieveQCTestById(int id) {
        logger.info("Executing retrieveQCTestById with id: {}", id);
        return repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Test not found"));
    }

    public List<QCTest> retrieveQCTestByBatchId(
            int batchId) {
        logger.info("Executing retrieveQCTestByBatchId with batchId: {}", batchId);
        List<QCTest> tests =
            repository.findByBatchId(batchId);
        if (tests.isEmpty()) {
            throw new RuntimeException(
                "No tests found for batch");
        }
        return tests;
    }

    public Map<String, String> createQCTest(
            QCTest test) {
        logger.info("Executing createQCTest with testType: {}", test.getTestType());
        test.setStatus("RT");
        repository.save(test);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "QC Test created successfully");
        return response;
    }

    public Map<String, String> updateQCTest(
            int id, QCTest test) {
        logger.info("Executing updateQCTest with id: {}", id);
        QCTest existing = repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Test not found"));
        existing.setBatchId(test.getBatchId());
        existing.setTestType(test.getTestType());
        existing.setTestedById(test.getTestedById());
        existing.setTestDate(test.getTestDate());
        existing.setResult(test.getResult());
        existing.setSpecification(
            test.getSpecification());
        repository.save(existing);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "QC Test updated successfully");
        return response;
    }

    public Map<String, String> updateQCTestStatus(
            int id, String newStatus) {
        logger.info("Executing updateQCTestStatus with id: {} and newStatus: {}", id, newStatus);
        QCTest existing = repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Test not found"));
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