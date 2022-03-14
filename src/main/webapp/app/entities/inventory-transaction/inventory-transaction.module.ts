import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InventoryTransactionComponent } from './list/inventory-transaction.component';
import { InventoryTransactionDetailComponent } from './detail/inventory-transaction-detail.component';
import { InventoryTransactionUpdateComponent } from './update/inventory-transaction-update.component';
import { InventoryTransactionDeleteDialogComponent } from './delete/inventory-transaction-delete-dialog.component';
import { InventoryTransactionRoutingModule } from './route/inventory-transaction-routing.module';

@NgModule({
  imports: [SharedModule, InventoryTransactionRoutingModule],
  declarations: [
    InventoryTransactionComponent,
    InventoryTransactionDetailComponent,
    InventoryTransactionUpdateComponent,
    InventoryTransactionDeleteDialogComponent,
  ],
  entryComponents: [InventoryTransactionDeleteDialogComponent],
})
export class InventoryTransactionModule {}
