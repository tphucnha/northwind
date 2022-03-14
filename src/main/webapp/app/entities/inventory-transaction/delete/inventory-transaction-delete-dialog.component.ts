import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInventoryTransaction } from '../inventory-transaction.model';
import { InventoryTransactionService } from '../service/inventory-transaction.service';

@Component({
  templateUrl: './inventory-transaction-delete-dialog.component.html',
})
export class InventoryTransactionDeleteDialogComponent {
  inventoryTransaction?: IInventoryTransaction;

  constructor(protected inventoryTransactionService: InventoryTransactionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inventoryTransactionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
