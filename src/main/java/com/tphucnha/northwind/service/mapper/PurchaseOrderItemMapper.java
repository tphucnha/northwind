package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.PurchaseOrderItem;
import com.tphucnha.northwind.service.dto.PurchaseOrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseOrderItem} and its DTO {@link PurchaseOrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, PurchaseOrderMapper.class })
public interface PurchaseOrderItemMapper extends EntityMapper<PurchaseOrderItemDTO, PurchaseOrderItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "purchaseOrder", source = "purchaseOrder", qualifiedByName = "id")
    PurchaseOrderItemDTO toDto(PurchaseOrderItem s);
}
