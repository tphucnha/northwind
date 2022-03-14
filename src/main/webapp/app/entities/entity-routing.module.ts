import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'northwindApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'northwindApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'inventory-transaction',
        data: { pageTitle: 'northwindApp.inventoryTransaction.home.title' },
        loadChildren: () => import('./inventory-transaction/inventory-transaction.module').then(m => m.InventoryTransactionModule),
      },
      {
        path: 'customer-order',
        data: { pageTitle: 'northwindApp.customerOrder.home.title' },
        loadChildren: () => import('./customer-order/customer-order.module').then(m => m.CustomerOrderModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'northwindApp.orderItem.home.title' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'northwindApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'purchase-order',
        data: { pageTitle: 'northwindApp.purchaseOrder.home.title' },
        loadChildren: () => import('./purchase-order/purchase-order.module').then(m => m.PurchaseOrderModule),
      },
      {
        path: 'purchase-order-item',
        data: { pageTitle: 'northwindApp.purchaseOrderItem.home.title' },
        loadChildren: () => import('./purchase-order-item/purchase-order-item.module').then(m => m.PurchaseOrderItemModule),
      },
      {
        path: 'supplier',
        data: { pageTitle: 'northwindApp.supplier.home.title' },
        loadChildren: () => import('./supplier/supplier.module').then(m => m.SupplierModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
