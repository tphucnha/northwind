package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.OrderItem;
import com.tphucnha.northwind.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductMapper.class, CustomerOrderMapper.class })
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "id")
    @Mapping(target = "order", source = "order", qualifiedByName = "id")
    OrderItemDTO toDto(OrderItem s);
}
