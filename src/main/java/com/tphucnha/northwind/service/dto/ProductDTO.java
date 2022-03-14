package com.tphucnha.northwind.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.tphucnha.northwind.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private String code;

    private String name;

    private String description;

    private BigDecimal standardCost;

    private BigDecimal listPrice;

    private Integer reorderLevel;

    private Integer targetLevel;

    private Integer quantityPerUnit;

    private Boolean disContinued;

    private Integer minimumReorderQuantity;

    private CategoryDTO category;

    private Set<SupplierDTO> suppliers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getStandardCost() {
        return standardCost;
    }

    public void setStandardCost(BigDecimal standardCost) {
        this.standardCost = standardCost;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public Integer getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(Integer reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public Integer getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(Integer targetLevel) {
        this.targetLevel = targetLevel;
    }

    public Integer getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public void setQuantityPerUnit(Integer quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public Boolean getDisContinued() {
        return disContinued;
    }

    public void setDisContinued(Boolean disContinued) {
        this.disContinued = disContinued;
    }

    public Integer getMinimumReorderQuantity() {
        return minimumReorderQuantity;
    }

    public void setMinimumReorderQuantity(Integer minimumReorderQuantity) {
        this.minimumReorderQuantity = minimumReorderQuantity;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public Set<SupplierDTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<SupplierDTO> suppliers) {
        this.suppliers = suppliers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
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
            ", category=" + getCategory() +
            ", suppliers=" + getSuppliers() +
            "}";
    }
}
