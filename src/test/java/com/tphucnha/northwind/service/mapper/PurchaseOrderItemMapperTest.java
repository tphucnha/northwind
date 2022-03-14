package com.tphucnha.northwind.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PurchaseOrderItemMapperTest {

    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    @BeforeEach
    public void setUp() {
        purchaseOrderItemMapper = new PurchaseOrderItemMapperImpl();
    }
}
