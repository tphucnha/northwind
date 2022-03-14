import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';

@Component({
  templateUrl: './purchase-order-item-delete-dialog.component.html',
})
export class PurchaseOrderItemDeleteDialogComponent {
  purchaseOrderItem?: IPurchaseOrderItem;

  constructor(protected purchaseOrderItemService: PurchaseOrderItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchaseOrderItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
