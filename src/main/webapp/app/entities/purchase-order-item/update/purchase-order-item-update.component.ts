import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPurchaseOrderItem, PurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

@Component({
  selector: 'jhi-purchase-order-item-update',
  templateUrl: './purchase-order-item-update.component.html',
})
export class PurchaseOrderItemUpdateComponent implements OnInit {
  isSaving = false;

  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [],
    unitCost: [],
    receivedDate: [],
    inventoryPosted: [],
    purchaseOrder: [],
  });

  constructor(
    protected purchaseOrderItemService: PurchaseOrderItemService,
    protected purchaseOrderService: PurchaseOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrderItem }) => {
      if (purchaseOrderItem.id === undefined) {
        const today = dayjs().startOf('day');
        purchaseOrderItem.receivedDate = today;
      }

      this.updateForm(purchaseOrderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrderItem = this.createFromForm();
    if (purchaseOrderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderItemService.update(purchaseOrderItem));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderItemService.create(purchaseOrderItem));
    }
  }

  trackPurchaseOrderById(index: number, item: IPurchaseOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrderItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(purchaseOrderItem: IPurchaseOrderItem): void {
    this.editForm.patchValue({
      id: purchaseOrderItem.id,
      quantity: purchaseOrderItem.quantity,
      unitCost: purchaseOrderItem.unitCost,
      receivedDate: purchaseOrderItem.receivedDate ? purchaseOrderItem.receivedDate.format(DATE_TIME_FORMAT) : null,
      inventoryPosted: purchaseOrderItem.inventoryPosted,
      purchaseOrder: purchaseOrderItem.purchaseOrder,
    });

    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(
      this.purchaseOrdersSharedCollection,
      purchaseOrderItem.purchaseOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.purchaseOrderService
      .query()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing(purchaseOrders, this.editForm.get('purchaseOrder')!.value)
        )
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));
  }

  protected createFromForm(): IPurchaseOrderItem {
    return {
      ...new PurchaseOrderItem(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      unitCost: this.editForm.get(['unitCost'])!.value,
      receivedDate: this.editForm.get(['receivedDate'])!.value
        ? dayjs(this.editForm.get(['receivedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      inventoryPosted: this.editForm.get(['inventoryPosted'])!.value,
      purchaseOrder: this.editForm.get(['purchaseOrder'])!.value,
    };
  }
}
