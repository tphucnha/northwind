package com.tphucnha.northwind.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tphucnha.northwind.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseOrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItem1 = new PurchaseOrderItem();
        purchaseOrderItem1.setId(1L);
        PurchaseOrderItem purchaseOrderItem2 = new PurchaseOrderItem();
        purchaseOrderItem2.setId(purchaseOrderItem1.getId());
        assertThat(purchaseOrderItem1).isEqualTo(purchaseOrderItem2);
        purchaseOrderItem2.setId(2L);
        assertThat(purchaseOrderItem1).isNotEqualTo(purchaseOrderItem2);
        purchaseOrderItem1.setId(null);
        assertThat(purchaseOrderItem1).isNotEqualTo(purchaseOrderItem2);
    }
}
