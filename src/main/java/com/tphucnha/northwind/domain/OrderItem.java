package com.tphucnha.northwind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tphucnha.northwind.domain.enumeration.OrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;

/**
 * A OrderItem.
 */
@Entity
@Table(name = "order_item")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "unit_price", precision = 21, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount")
    private Long discount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderItemStatus status;

    @Column(name = "allocated_date")
    private Instant allocatedDate;

    @JsonIgnoreProperties(value = { "category", "suppliers" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Product product;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orderItems", "customer" }, allowSetters = true)
    private CustomerOrder order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public OrderItem quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public OrderItem unitPrice(BigDecimal unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getDiscount() {
        return this.discount;
    }

    public OrderItem discount(Long discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public OrderItemStatus getStatus() {
        return this.status;
    }

    public OrderItem status(OrderItemStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Instant getAllocatedDate() {
        return this.allocatedDate;
    }

    public OrderItem allocatedDate(Instant allocatedDate) {
        this.setAllocatedDate(allocatedDate);
        return this;
    }

    public void setAllocatedDate(Instant allocatedDate) {
        this.allocatedDate = allocatedDate;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public CustomerOrder getOrder() {
        return this.order;
    }

    public void setOrder(CustomerOrder customerOrder) {
        this.order = customerOrder;
    }

    public OrderItem order(CustomerOrder customerOrder) {
        this.setOrder(customerOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        return id != null && id.equals(((OrderItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", discount=" + getDiscount() +
            ", status='" + getStatus() + "'" +
            ", allocatedDate='" + getAllocatedDate() + "'" +
            "}";
    }
}
