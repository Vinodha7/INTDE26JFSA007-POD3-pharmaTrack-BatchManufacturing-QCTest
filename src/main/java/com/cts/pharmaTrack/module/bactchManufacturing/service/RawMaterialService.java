package com.cts.pharmaTrack.module
    .batchManufacturing.service;

import com.cts.pharmaTrack.module
    .batchManufacturing.entity.RawMaterialUsage;
import com.cts.pharmaTrack.module
    .batchManufacturing.repository
    .RawMaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RawMaterialService {
    private static final Logger logger = LoggerFactory.getLogger(RawMaterialService.class);

    private final RawMaterialRepository repository;

    public RawMaterialService(RawMaterialRepository repository) {
        this.repository = repository;
    }

    private static final Map<String, List<String>>
        TRANSITIONS = new HashMap<>();
    static {
        TRANSITIONS.put("CON", List.of("QRN", "DEL"));
        TRANSITIONS.put("QRN", List.of("CON", "DEL"));
    }

    public List<RawMaterialUsage>
            retrieveRawMaterials() {
        logger.info("Executing retrieveRawMaterials");
        return repository.findAll();
    }

    public RawMaterialUsage retrieveRawMaterialById(
            int id) {
        logger.info("Executing retrieveRawMaterialById with id: {}", id);
        return repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Material not found"));
    }

    public List<RawMaterialUsage>
            retrieveRawMaterialByBatchId(int batchId) {
        logger.info("Executing retrieveRawMaterialByBatchId with batchId: {}", batchId);
        List<RawMaterialUsage> materials =
            repository.findByBatchId(batchId);
        if (materials.isEmpty()) {
            throw new RuntimeException(
                "No materials found for batch");
        }
        return materials;
    }

    public Map<String, String> createRawMaterial(
            RawMaterialUsage material) {
        logger.info("Executing createRawMaterial with materialName: {}", material.getMaterialName());
        material.setStatus("CON");
        repository.save(material);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Material created successfully");
        return response;
    }

    public Map<String, String> updateRawMaterial(
            int id, RawMaterialUsage material) {
        logger.info("Executing updateRawMaterial with id: {}", id);
        RawMaterialUsage existing =
            repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Material not found"));
        existing.setBatchId(material.getBatchId());
        existing.setMaterialName(
            material.getMaterialName());
        existing.setMaterialLotNumber(
            material.getMaterialLotNumber());
        existing.setQuantityUsed(
            material.getQuantityUsed());
        existing.setUnit(material.getUnit());
        repository.save(existing);
        Map<String, String> response = new HashMap<>();
        response.put("message",
            "Material updated successfully");
        return response;
    }

    public Map<String, String> updateRawMaterialStatus(
            int id, String newStatus) {
        logger.info("Executing updateRawMaterialStatus with id: {} and newStatus: {}", id, newStatus);
        RawMaterialUsage existing =
            repository.findById(id)
            .orElseThrow(() ->
                new RuntimeException(
                    "Material not found"));
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