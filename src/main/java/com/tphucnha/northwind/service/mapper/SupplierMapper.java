package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.Supplier;
import com.tphucnha.northwind.service.dto.SupplierDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Supplier} and its DTO {@link SupplierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SupplierMapper extends EntityMapper<SupplierDTO, Supplier> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SupplierDTO toDtoId(Supplier supplier);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SupplierDTO> toDtoIdSet(Set<Supplier> supplier);
}
