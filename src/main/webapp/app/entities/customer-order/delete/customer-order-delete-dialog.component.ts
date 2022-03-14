import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomerOrder } from '../customer-order.model';
import { CustomerOrderService } from '../service/customer-order.service';

@Component({
  templateUrl: './customer-order-delete-dialog.component.html',
})
export class CustomerOrderDeleteDialogComponent {
  customerOrder?: ICustomerOrder;

  constructor(protected customerOrderService: CustomerOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.customerOrderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
