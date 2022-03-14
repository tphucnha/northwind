import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PurchaseOrderItemComponent } from './list/purchase-order-item.component';
import { PurchaseOrderItemDetailComponent } from './detail/purchase-order-item-detail.component';
import { PurchaseOrderItemUpdateComponent } from './update/purchase-order-item-update.component';
import { PurchaseOrderItemDeleteDialogComponent } from './delete/purchase-order-item-delete-dialog.component';
import { PurchaseOrderItemRoutingModule } from './route/purchase-order-item-routing.module';

@NgModule({
  imports: [SharedModule, PurchaseOrderItemRoutingModule],
  declarations: [
    PurchaseOrderItemComponent,
    PurchaseOrderItemDetailComponent,
    PurchaseOrderItemUpdateComponent,
    PurchaseOrderItemDeleteDialogComponent,
  ],
  entryComponents: [PurchaseOrderItemDeleteDialogComponent],
})
export class PurchaseOrderItemModule {}
