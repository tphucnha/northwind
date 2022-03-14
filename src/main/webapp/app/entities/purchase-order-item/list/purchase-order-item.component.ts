import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';
import { PurchaseOrderItemDeleteDialogComponent } from '../delete/purchase-order-item-delete-dialog.component';

@Component({
  selector: 'jhi-purchase-order-item',
  templateUrl: './purchase-order-item.component.html',
})
export class PurchaseOrderItemComponent implements OnInit {
  purchaseOrderItems?: IPurchaseOrderItem[];
  isLoading = false;

  constructor(protected purchaseOrderItemService: PurchaseOrderItemService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.purchaseOrderItemService.query().subscribe({
      next: (res: HttpResponse<IPurchaseOrderItem[]>) => {
        this.isLoading = false;
        this.purchaseOrderItems = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPurchaseOrderItem): number {
    return item.id!;
  }

  delete(purchaseOrderItem: IPurchaseOrderItem): void {
    const modalRef = this.modalService.open(PurchaseOrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.purchaseOrderItem = purchaseOrderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
