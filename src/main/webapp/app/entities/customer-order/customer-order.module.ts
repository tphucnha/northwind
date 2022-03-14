import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CustomerOrderComponent } from './list/customer-order.component';
import { CustomerOrderDetailComponent } from './detail/customer-order-detail.component';
import { CustomerOrderUpdateComponent } from './update/customer-order-update.component';
import { CustomerOrderDeleteDialogComponent } from './delete/customer-order-delete-dialog.component';
import { CustomerOrderRoutingModule } from './route/customer-order-routing.module';

@NgModule({
  imports: [SharedModule, CustomerOrderRoutingModule],
  declarations: [CustomerOrderComponent, CustomerOrderDetailComponent, CustomerOrderUpdateComponent, CustomerOrderDeleteDialogComponent],
  entryComponents: [CustomerOrderDeleteDialogComponent],
})
export class CustomerOrderModule {}
