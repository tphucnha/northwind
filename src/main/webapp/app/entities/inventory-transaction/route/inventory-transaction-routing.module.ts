import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InventoryTransactionComponent } from '../list/inventory-transaction.component';
import { InventoryTransactionDetailComponent } from '../detail/inventory-transaction-detail.component';
import { InventoryTransactionUpdateComponent } from '../update/inventory-transaction-update.component';
import { InventoryTransactionRoutingResolveService } from './inventory-transaction-routing-resolve.service';

const inventoryTransactionRoute: Routes = [
  {
    path: '',
    component: InventoryTransactionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InventoryTransactionDetailComponent,
    resolve: {
      inventoryTransaction: InventoryTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InventoryTransactionUpdateComponent,
    resolve: {
      inventoryTransaction: InventoryTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InventoryTransactionUpdateComponent,
    resolve: {
      inventoryTransaction: InventoryTransactionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(inventoryTransactionRoute)],
  exports: [RouterModule],
})
export class InventoryTransactionRoutingModule {}
