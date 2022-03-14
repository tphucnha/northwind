import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PurchaseOrderItemComponent } from '../list/purchase-order-item.component';
import { PurchaseOrderItemDetailComponent } from '../detail/purchase-order-item-detail.component';
import { PurchaseOrderItemUpdateComponent } from '../update/purchase-order-item-update.component';
import { PurchaseOrderItemRoutingResolveService } from './purchase-order-item-routing-resolve.service';

const purchaseOrderItemRoute: Routes = [
  {
    path: '',
    component: PurchaseOrderItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PurchaseOrderItemDetailComponent,
    resolve: {
      purchaseOrderItem: PurchaseOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PurchaseOrderItemUpdateComponent,
    resolve: {
      purchaseOrderItem: PurchaseOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PurchaseOrderItemUpdateComponent,
    resolve: {
      purchaseOrderItem: PurchaseOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(purchaseOrderItemRoute)],
  exports: [RouterModule],
})
export class PurchaseOrderItemRoutingModule {}
