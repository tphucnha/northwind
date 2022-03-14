package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.PurchaseOrder;
import com.tphucnha.northwind.service.dto.PurchaseOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseOrder} and its DTO {@link PurchaseOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { SupplierMapper.class })
public interface PurchaseOrderMapper extends EntityMapper<PurchaseOrderDTO, PurchaseOrder> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "id")
    PurchaseOrderDTO toDto(PurchaseOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PurchaseOrderDTO toDtoId(PurchaseOrder purchaseOrder);
}
