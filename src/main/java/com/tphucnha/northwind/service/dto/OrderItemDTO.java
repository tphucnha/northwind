package com.tphucnha.northwind.service.dto;

import com.tphucnha.northwind.domain.enumeration.OrderItemStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.tphucnha.northwind.domain.OrderItem} entity.
 */
public class OrderItemDTO implements Serializable {

    private Long id;

    private Long quantity;

    private BigDecimal unitPrice;

    private Long discount;

    private OrderItemStatus status;

    private Instant allocatedDate;

    private ProductDTO product;

    private CustomerOrderDTO order;

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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public Instant getAllocatedDate() {
        return allocatedDate;
    }

    public void setAllocatedDate(Instant allocatedDate) {
        this.allocatedDate = allocatedDate;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public CustomerOrderDTO getOrder() {
        return order;
    }

    public void setOrder(CustomerOrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItemDTO)) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", discount=" + getDiscount() +
            ", status='" + getStatus() + "'" +
            ", allocatedDate='" + getAllocatedDate() + "'" +
            ", product=" + getProduct() +
            ", order=" + getOrder() +
            "}";
    }
}
