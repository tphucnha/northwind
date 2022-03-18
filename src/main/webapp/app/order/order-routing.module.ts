import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OrderComponent} from "./list/order.component";
import {OrderUpdateComponent} from "./update/order-update.component";
import {
  CustomerOrderRoutingResolveService
} from "../entities/customer-order/route/customer-order-routing-resolve.service";
import {UserRouteAccessService} from "../core/auth/user-route-access.service";
import {OrderDetailComponent} from "./detail/order-detail.component";

const routes: Routes = [
  {path: '', component: OrderComponent},
  {
    path: ':id/view',
    component: OrderDetailComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new', component: OrderUpdateComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: OrderUpdateComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule {
}
