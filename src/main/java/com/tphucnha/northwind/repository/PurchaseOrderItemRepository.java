package com.tphucnha.northwind.repository;

import com.tphucnha.northwind.domain.PurchaseOrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PurchaseOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {}
