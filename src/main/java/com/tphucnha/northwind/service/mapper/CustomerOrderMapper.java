package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.CustomerOrder;
import com.tphucnha.northwind.domain.OrderItem;
import com.tphucnha.northwind.service.dto.CustomerOrderDTO;
import com.tphucnha.northwind.service.dto.OrderItemDTO;
import org.mapstruct.*;

import java.util.Set;

/**
 * Mapper for the entity {@link CustomerOrder} and its DTO {@link CustomerOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, OrderItemMapper.class})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerOrderDTO toDtoId(CustomerOrder customerOrder);
}
