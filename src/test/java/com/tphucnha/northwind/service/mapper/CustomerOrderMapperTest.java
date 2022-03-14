package com.tphucnha.northwind.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerOrderMapperTest {

    private CustomerOrderMapper customerOrderMapper;

    @BeforeEach
    public void setUp() {
        customerOrderMapper = new CustomerOrderMapperImpl();
    }
}
