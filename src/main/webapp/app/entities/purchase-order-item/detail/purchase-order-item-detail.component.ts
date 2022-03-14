import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPurchaseOrderItem } from '../purchase-order-item.model';

@Component({
  selector: 'jhi-purchase-order-item-detail',
  templateUrl: './purchase-order-item-detail.component.html',
})
export class PurchaseOrderItemDetailComponent implements OnInit {
  purchaseOrderItem: IPurchaseOrderItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrderItem }) => {
      this.purchaseOrderItem = purchaseOrderItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
