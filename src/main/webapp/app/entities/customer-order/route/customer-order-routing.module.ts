import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CustomerOrderComponent } from '../list/customer-order.component';
import { CustomerOrderDetailComponent } from '../detail/customer-order-detail.component';
import { CustomerOrderUpdateComponent } from '../update/customer-order-update.component';
import { CustomerOrderRoutingResolveService } from './customer-order-routing-resolve.service';

const customerOrderRoute: Routes = [
  {
    path: '',
    component: CustomerOrderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CustomerOrderDetailComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CustomerOrderUpdateComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CustomerOrderUpdateComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerOrderRoute)],
  exports: [RouterModule],
})
export class CustomerOrderRoutingModule {}
