import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {OrderRoutingModule} from './order-routing.module';
import {OrderComponent} from "./list/order.component";
import {OrderUpdateComponent} from './update/order-update.component';
import {SharedModule} from "../shared/shared.module";


@NgModule({
  declarations: [OrderComponent, OrderUpdateComponent],
  imports: [
    CommonModule,
    OrderRoutingModule,
    SharedModule
  ]
})
export class OrderModule {
}
