package com.tphucnha.northwind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tphucnha.northwind.domain.enumeration.PurchaseOrderStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PurchaseOrderStatus status;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "expected_date")
    private Instant expectedDate;

    @Column(name = "payment_date")
    private Instant paymentDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @OneToMany(mappedBy = "purchaseOrder")
    @JsonIgnoreProperties(value = { "product", "purchaseOrder" }, allowSetters = true)
    private Set<PurchaseOrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Supplier supplier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PurchaseOrderStatus getStatus() {
        return this.status;
    }

    public PurchaseOrder status(PurchaseOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public PurchaseOrder createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getExpectedDate() {
        return this.expectedDate;
    }

    public PurchaseOrder expectedDate(Instant expectedDate) {
        this.setExpectedDate(expectedDate);
        return this;
    }

    public void setExpectedDate(Instant expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public PurchaseOrder paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public PurchaseOrder paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }

    public PurchaseOrder paymentAmount(BigDecimal paymentAmount) {
        this.setPaymentAmount(paymentAmount);
        return this;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Set<PurchaseOrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void setOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setPurchaseOrder(null));
        }
        if (purchaseOrderItems != null) {
            purchaseOrderItems.forEach(i -> i.setPurchaseOrder(this));
        }
        this.orderItems = purchaseOrderItems;
    }

    public PurchaseOrder orderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        this.setOrderItems(purchaseOrderItems);
        return this;
    }

    public PurchaseOrder addOrderItems(PurchaseOrderItem purchaseOrderItem) {
        this.orderItems.add(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(this);
        return this;
    }

    public PurchaseOrder removeOrderItems(PurchaseOrderItem purchaseOrderItem) {
        this.orderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(null);
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public PurchaseOrder supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", expectedDate='" + getExpectedDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentAmount=" + getPaymentAmount() +
            "}";
    }
}
