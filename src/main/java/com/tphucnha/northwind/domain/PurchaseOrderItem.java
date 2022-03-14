package com.tphucnha.northwind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PurchaseOrderItem.
 */
@Entity
@Table(name = "purchase_order_item")
public class PurchaseOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "unit_cost", precision = 21, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "received_date")
    private Instant receivedDate;

    @Column(name = "inventory_posted")
    private Boolean inventoryPosted;

    @JsonIgnoreProperties(value = { "category", "suppliers" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "orderItems", "supplier" }, allowSetters = true)
    private PurchaseOrder purchaseOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public PurchaseOrderItem quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitCost() {
        return this.unitCost;
    }

    public PurchaseOrderItem unitCost(BigDecimal unitCost) {
        this.setUnitCost(unitCost);
        return this;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public Instant getReceivedDate() {
        return this.receivedDate;
    }

    public PurchaseOrderItem receivedDate(Instant receivedDate) {
        this.setReceivedDate(receivedDate);
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Boolean getInventoryPosted() {
        return this.inventoryPosted;
    }

    public PurchaseOrderItem inventoryPosted(Boolean inventoryPosted) {
        this.setInventoryPosted(inventoryPosted);
        return this;
    }

    public void setInventoryPosted(Boolean inventoryPosted) {
        this.inventoryPosted = inventoryPosted;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PurchaseOrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderItem purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderItem)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitCost=" + getUnitCost() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", inventoryPosted='" + getInventoryPosted() + "'" +
            "}";
    }
}
