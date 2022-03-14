package com.tphucnha.northwind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

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

    @OneToMany(mappedBy = "purchaseOrderItem")
    @JsonIgnoreProperties(
        value = { "category", "suppliers", "orderItem", "purchaseOrderItem", "inventoryTransaction" },
        allowSetters = true
    )
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderItems", "supplier", "inventoryTransaction" }, allowSetters = true)
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

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setPurchaseOrderItem(null));
        }
        if (products != null) {
            products.forEach(i -> i.setPurchaseOrderItem(this));
        }
        this.products = products;
    }

    public PurchaseOrderItem products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public PurchaseOrderItem addProduct(Product product) {
        this.products.add(product);
        product.setPurchaseOrderItem(this);
        return this;
    }

    public PurchaseOrderItem removeProduct(Product product) {
        this.products.remove(product);
        product.setPurchaseOrderItem(null);
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
