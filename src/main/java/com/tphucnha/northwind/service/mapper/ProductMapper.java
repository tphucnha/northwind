package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.Product;
import com.tphucnha.northwind.service.dto.ProductDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        CategoryMapper.class, SupplierMapper.class, OrderItemMapper.class, PurchaseOrderItemMapper.class, InventoryTransactionMapper.class,
    }
)
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "idSet")
    @Mapping(target = "orderItem", source = "orderItem", qualifiedByName = "id")
    @Mapping(target = "purchaseOrderItem", source = "purchaseOrderItem", qualifiedByName = "id")
    @Mapping(target = "inventoryTransaction", source = "inventoryTransaction", qualifiedByName = "id")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeSuppliers", ignore = true)
    Product toEntity(ProductDTO productDTO);
}
