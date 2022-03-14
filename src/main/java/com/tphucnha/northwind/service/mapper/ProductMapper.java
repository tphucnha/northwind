package com.tphucnha.northwind.service.mapper;

import com.tphucnha.northwind.domain.Product;
import com.tphucnha.northwind.service.dto.ProductDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CategoryMapper.class, SupplierMapper.class })
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    @Mapping(target = "suppliers", source = "suppliers", qualifiedByName = "idSet")
    ProductDTO toDto(Product s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoId(Product product);

    @Mapping(target = "removeSuppliers", ignore = true)
    Product toEntity(ProductDTO productDTO);
}
