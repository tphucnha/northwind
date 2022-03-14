package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.InventoryTransaction;
import com.tphucnha.northwind.service.dto.InventoryTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoryTransaction} and its DTO {@link InventoryTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InventoryTransactionMapper extends EntityMapper<InventoryTransactionDTO, InventoryTransaction> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InventoryTransactionDTO toDtoId(InventoryTransaction inventoryTransaction);
}
