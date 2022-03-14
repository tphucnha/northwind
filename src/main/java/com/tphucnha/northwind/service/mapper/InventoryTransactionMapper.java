package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.InventoryTransaction;
import com.tphucnha.northwind.service.dto.InventoryTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoryTransaction} and its DTO {@link InventoryTransactionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface InventoryTransactionMapper extends EntityMapper<InventoryTransactionDTO, InventoryTransaction> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    InventoryTransactionDTO toDto(InventoryTransaction s);
}
