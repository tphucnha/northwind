package com.tphucnha.northwind.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventoryTransactionMapperTest {

    private InventoryTransactionMapper inventoryTransactionMapper;

    @BeforeEach
    public void setUp() {
        inventoryTransactionMapper = new InventoryTransactionMapperImpl();
    }
}
