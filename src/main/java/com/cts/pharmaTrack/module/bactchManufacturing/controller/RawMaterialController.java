package com.cts.pharmaTrack.module
    .batchManufacturing.controller;

import com.cts.pharmaTrack.module
    .batchManufacturing.entity.RawMaterialUsage;
import com.cts.pharmaTrack.module
    .batchManufacturing.service.RawMaterialService;
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
public class RawMaterialController {
    private static final Logger logger = LoggerFactory.getLogger(RawMaterialController.class);

    private final RawMaterialService service;

    public RawMaterialController(RawMaterialService service) {
        this.service = service;
    }

    @PostMapping("/createRawMaterial")
    public ResponseEntity<?> createRawMaterial(
            @RequestBody RawMaterialUsage material) {
        logger.info("POST /createRawMaterial request received with materialName: {}", material.getMaterialName());
        try {
            Map<String, String> response =
                service.createRawMaterial(material);
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

    @GetMapping("/retrieveRawMaterials")
    public ResponseEntity<?> retrieveRawMaterials() {
        logger.info("GET /retrieveRawMaterials request received");
        try {
            List<RawMaterialUsage> materials =
                service.retrieveRawMaterials();
            if (materials.isEmpty()) {
                return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message",
                        "No materials found"));
            }
            return ResponseEntity.ok(materials);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus
                    .INTERNAL_SERVER_ERROR)
                .body(Map.of("message",
                    e.getMessage()));
        }
    }

    @GetMapping(
        "/retrieveRawMaterialById/{usage_id}")
    public ResponseEntity<?> retrieveRawMaterialById(
            @PathVariable("usage_id") int usageId) {
        logger.info("GET /retrieveRawMaterialById/{} request received with usageId: {}", usageId, usageId);
        try {
            RawMaterialUsage material =
                service.retrieveRawMaterialById(
                    usageId);
            return ResponseEntity.ok(material);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "Material not found"));
        }
    }

    @GetMapping(
        "/retrieveRawMaterialByBatchId/{batch_id}")
    public ResponseEntity<?>
            retrieveRawMaterialByBatchId(
            @PathVariable("batch_id") int batchId) {
        logger.info("GET /retrieveRawMaterialByBatchId/{} request received with batchId: {}", batchId, batchId);
        try {
            List<RawMaterialUsage> materials =
                service.retrieveRawMaterialByBatchId(
                    batchId);
            return ResponseEntity.ok(materials);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "No materials found"));
        }
    }

    @PutMapping("/updateRawMaterial/{usage_id}")
    public ResponseEntity<?> updateRawMaterial(
            @PathVariable("usage_id") int usageId,
            @RequestBody RawMaterialUsage material) {
        logger.info("PUT /updateRawMaterial/{} request received with usageId: {}", usageId, usageId);
        try {
            Map<String, String> response =
                service.updateRawMaterial(
                    usageId, material);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message",
                    "Material not found"));
        }
    }

    @PutMapping(
        "/updateRawMaterialStatus/{usage_id}")
    public ResponseEntity<?> updateRawMaterialStatus(
            @PathVariable("usage_id") int usageId,
            @RequestBody Map<String, String> body) {
        logger.info("PUT /updateRawMaterialStatus/{} request received with usageId: {}", usageId, usageId);
        try {
            String newStatus = body.get("status");
            Map<String, String> response =
                service.updateRawMaterialStatus(
                    usageId, newStatus);
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