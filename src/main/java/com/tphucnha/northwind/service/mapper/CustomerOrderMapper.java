package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.CustomerOrder;
import com.tphucnha.northwind.service.dto.CustomerOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerOrder} and its DTO {@link CustomerOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "id")
    CustomerOrderDTO toDto(CustomerOrder s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerOrderDTO toDtoId(CustomerOrder customerOrder);
}
