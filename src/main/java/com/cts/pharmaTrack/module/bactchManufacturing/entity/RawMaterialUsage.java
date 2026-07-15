package com.cts.pharmaTrack.module
    .batchManufacturing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "raw_material_usage")
public class RawMaterialUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usage_id")
    private int usageId;

    @Column(name = "batch_id", nullable = false)
    private int batchId;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "material_lot_number",
            nullable = false)
    private String materialLotNumber;

    @Column(name = "quantity_used", nullable = false)
    private double quantityUsed;

    @Column(name = "unit")
    private String unit;

    @Column(name = "status")
    private String status = "CON";

    public int getUsageId() { return usageId; }
    public void setUsageId(int usageId) {
        this.usageId = usageId; }
    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) {
        this.batchId = batchId; }
    public String getMaterialName() {
        return materialName; }
    public void setMaterialName(String materialName) {
        this.materialName = materialName; }
    public String getMaterialLotNumber() {
        return materialLotNumber; }
    public void setMaterialLotNumber(
            String materialLotNumber) {
        this.materialLotNumber = materialLotNumber; }
    public double getQuantityUsed() {
        return quantityUsed; }
    public void setQuantityUsed(double quantityUsed) {
        this.quantityUsed = quantityUsed; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) {
        this.unit = unit; }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status; }
}