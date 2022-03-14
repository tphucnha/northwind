package com.tphucnha.northwind.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "standard_cost", precision = 21, scale = 2)
    private BigDecimal standardCost;

    @Column(name = "list_price", precision = 21, scale = 2)
    private BigDecimal listPrice;

    @Column(name = "reorder_level")
    private Integer reorderLevel;

    @Column(name = "target_level")
    private Integer targetLevel;

    @Column(name = "quantity_per_unit")
    private Integer quantityPerUnit;

    @Column(name = "dis_continued")
    private Boolean disContinued;

    @Column(name = "minimum_reorder_quantity")
    private Integer minimumReorderQuantity;

    @ManyToOne
    private Category category;

    @ManyToMany
    @JoinTable(
        name = "rel_product__suppliers",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "suppliers_id")
    )
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Supplier> suppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Product code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStandardCost() {
        return this.standardCost;
    }

    public Product standardCost(BigDecimal standardCost) {
        this.setStandardCost(standardCost);
        return this;
    }

    public void setStandardCost(BigDecimal standardCost) {
        this.standardCost = standardCost;
    }

    public BigDecimal getListPrice() {
        return this.listPrice;
    }

    public Product listPrice(BigDecimal listPrice) {
        this.setListPrice(listPrice);
        return this;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public Integer getReorderLevel() {
        return this.reorderLevel;
    }

    public Product reorderLevel(Integer reorderLevel) {
        this.setReorderLevel(reorderLevel);
        return this;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getTargetLevel() {
        return this.targetLevel;
    }

    public Product targetLevel(Integer targetLevel) {
        this.setTargetLevel(targetLevel);
        return this;
    }

    public void setTargetLevel(Integer targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Integer getQuantityPerUnit() {
        return this.quantityPerUnit;
    }

    public Product quantityPerUnit(Integer quantityPerUnit) {
        this.setQuantityPerUnit(quantityPerUnit);
        return this;
    }

    public void setQuantityPerUnit(Integer quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public Boolean getDisContinued() {
        return this.disContinued;
    }

    public Product disContinued(Boolean disContinued) {
        this.setDisContinued(disContinued);
        return this;
    }

    public void setDisContinued(Boolean disContinued) {
        this.disContinued = disContinued;
    }

    public Integer getMinimumReorderQuantity() {
        return this.minimumReorderQuantity;
    }

    public Product minimumReorderQuantity(Integer minimumReorderQuantity) {
        this.setMinimumReorderQuantity(minimumReorderQuantity);
        return this;
    }

    public void setMinimumReorderQuantity(Integer minimumReorderQuantity) {
        this.minimumReorderQuantity = minimumReorderQuantity;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    public Set<Supplier> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public Product suppliers(Set<Supplier> suppliers) {
        this.setSuppliers(suppliers);
        return this;
    }

    public Product addSuppliers(Supplier supplier) {
        this.suppliers.add(supplier);
        supplier.getProducts().add(this);
        return this;
    }

    public Product removeSuppliers(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", standardCost=" + getStandardCost() +
            ", listPrice=" + getListPrice() +
            ", reorderLevel=" + getReorderLevel() +
            ", targetLevel=" + getTargetLevel() +
            ", quantityPerUnit=" + getQuantityPerUnit() +
            ", disContinued='" + getDisContinued() + "'" +
            ", minimumReorderQuantity=" + getMinimumReorderQuantity() +
            "}";
    }
}
