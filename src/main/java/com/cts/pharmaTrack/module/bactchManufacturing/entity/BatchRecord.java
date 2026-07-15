package com.cts.pharmaTrack.module
    .batchManufacturing.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "batch_record")
public class BatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_id")
    private int batchId;

    @Column(name = "product_id", nullable = false)
    private int productId;

    @Column(name = "batch_number",
            nullable = false, unique = true)
    private String batchNumber;

    @Column(name = "manufacturing_date",
            nullable = false)
    private LocalDate manufacturingDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "quantity_manufactured",
            nullable = false)
    private double quantityManufactured;

    @Column(name = "unit")
    private String unit;

    @Column(name = "manufacturing_site_id",
            nullable = false)
    private int manufacturingSiteId;

    @Column(name = "status")
    private String status = "IP";

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) {
        this.batchId = batchId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) {
        this.productId = productId; }
    public String getBatchNumber() {
        return batchNumber; }
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber; }
    public LocalDate getManufacturingDate() {
        return manufacturingDate; }
    public void setManufacturingDate(
            LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate; }
    public LocalDate getExpiryDate() {
        return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate; }
    public double getQuantityManufactured() {
        return quantityManufactured; }
    public void setQuantityManufactured(
            double quantityManufactured) {
        this.quantityManufactured =
            quantityManufactured; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) {
        this.unit = unit; }
    public int getManufacturingSiteId() {
        return manufacturingSiteId; }
    public void setManufacturingSiteId(
            int manufacturingSiteId) {
        this.manufacturingSiteId =
            manufacturingSiteId; }
    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status; }
}