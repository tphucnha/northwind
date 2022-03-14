package com.tphucnha.northwind.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.tphucnha.northwind.domain.PurchaseOrderItem} entity.
 */
public class PurchaseOrderItemDTO implements Serializable {

    private Long id;

    private Long quantity;

    private BigDecimal unitCost;

    private Instant receivedDate;

    private Boolean inventoryPosted;

    private PurchaseOrderDTO purchaseOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Boolean getInventoryPosted() {
        return inventoryPosted;
    }

    public void setInventoryPosted(Boolean inventoryPosted) {
        this.inventoryPosted = inventoryPosted;
    }

    public PurchaseOrderDTO getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderItemDTO)) {
            return false;
        }

        PurchaseOrderItemDTO purchaseOrderItemDTO = (PurchaseOrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, purchaseOrderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitCost=" + getUnitCost() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", inventoryPosted='" + getInventoryPosted() + "'" +
            ", purchaseOrder=" + getPurchaseOrder() +
            "}";
    }
}
