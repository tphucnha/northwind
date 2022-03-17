import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {OrderComponent} from "./list/order.component";
import {OrderUpdateComponent} from "./update/order-update.component";
import {
  CustomerOrderRoutingResolveService
} from "../entities/customer-order/route/customer-order-routing-resolve.service";

const routes: Routes = [
  {path: '', component: OrderComponent},
  {
    path: 'new', component: OrderUpdateComponent,
    resolve: {
      customerOrder: CustomerOrderRoutingResolveService,
    },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderRoutingModule {
}
