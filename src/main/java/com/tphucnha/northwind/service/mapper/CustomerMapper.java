package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.Customer;
import com.tphucnha.northwind.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerOrderMapper.class })
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    CustomerDTO toDto(Customer s);
}
