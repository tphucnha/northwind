package com.tphucnha.northwind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tphucnha.northwind.domain.enumeration.InventoryTransactionType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A InventoryTransaction.
 */
@Entity
@Table(name = "inventory_transaction")
public class InventoryTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private InventoryTransactionType transactionType;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "modified_date")
    private Instant modifiedDate;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "inventoryTransaction")
    @JsonIgnoreProperties(value = { "customers", "orderItems", "inventoryTransaction" }, allowSetters = true)
    private Set<CustomerOrder> orders = new HashSet<>();

    @OneToMany(mappedBy = "inventoryTransaction")
    @JsonIgnoreProperties(
        value = { "category", "suppliers", "orderItem", "purchaseOrderItem", "inventoryTransaction" },
        allowSetters = true
    )
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "inventoryTransaction")
    @JsonIgnoreProperties(value = { "orderItems", "supplier", "inventoryTransaction" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InventoryTransaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryTransactionType getTransactionType() {
        return this.transactionType;
    }

    public InventoryTransaction transactionType(InventoryTransactionType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(InventoryTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public InventoryTransaction createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getModifiedDate() {
        return this.modifiedDate;
    }

    public InventoryTransaction modifiedDate(Instant modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(Instant modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public InventoryTransaction quantity(Long quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return this.comments;
    }

    public InventoryTransaction comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<CustomerOrder> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<CustomerOrder> customerOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setInventoryTransaction(null));
        }
        if (customerOrders != null) {
            customerOrders.forEach(i -> i.setInventoryTransaction(this));
        }
        this.orders = customerOrders;
    }

    public InventoryTransaction orders(Set<CustomerOrder> customerOrders) {
        this.setOrders(customerOrders);
        return this;
    }

    public InventoryTransaction addOrder(CustomerOrder customerOrder) {
        this.orders.add(customerOrder);
        customerOrder.setInventoryTransaction(this);
        return this;
    }

    public InventoryTransaction removeOrder(CustomerOrder customerOrder) {
        this.orders.remove(customerOrder);
        customerOrder.setInventoryTransaction(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setInventoryTransaction(null));
        }
        if (products != null) {
            products.forEach(i -> i.setInventoryTransaction(this));
        }
        this.products = products;
    }

    public InventoryTransaction products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public InventoryTransaction addProduct(Product product) {
        this.products.add(product);
        product.setInventoryTransaction(this);
        return this;
    }

    public InventoryTransaction removeProduct(Product product) {
        this.products.remove(product);
        product.setInventoryTransaction(null);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        if (this.purchaseOrders != null) {
            this.purchaseOrders.forEach(i -> i.setInventoryTransaction(null));
        }
        if (purchaseOrders != null) {
            purchaseOrders.forEach(i -> i.setInventoryTransaction(this));
        }
        this.purchaseOrders = purchaseOrders;
    }

    public InventoryTransaction purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public InventoryTransaction addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        purchaseOrder.setInventoryTransaction(this);
        return this;
    }

    public InventoryTransaction removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        purchaseOrder.setInventoryTransaction(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventoryTransaction)) {
            return false;
        }
        return id != null && id.equals(((InventoryTransaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventoryTransaction{" +
            "id=" + getId() +
            ", transactionType='" + getTransactionType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", quantity=" + getQuantity() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
