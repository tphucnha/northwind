import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPurchaseOrder, PurchaseOrder } from '../purchase-order.model';
import { PurchaseOrderService } from '../service/purchase-order.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { PurchaseOrderStatus } from 'app/entities/enumerations/purchase-order-status.model';

@Component({
  selector: 'jhi-purchase-order-update',
  templateUrl: './purchase-order-update.component.html',
})
export class PurchaseOrderUpdateComponent implements OnInit {
  isSaving = false;
  purchaseOrderStatusValues = Object.keys(PurchaseOrderStatus);

  suppliersSharedCollection: ISupplier[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    createDate: [],
    expectedDate: [],
    paymentDate: [],
    paymentMethod: [],
    paymentAmount: [],
    supplier: [],
  });

  constructor(
    protected purchaseOrderService: PurchaseOrderService,
    protected supplierService: SupplierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrder }) => {
      if (purchaseOrder.id === undefined) {
        const today = dayjs().startOf('day');
        purchaseOrder.createDate = today;
        purchaseOrder.expectedDate = today;
        purchaseOrder.paymentDate = today;
      }

      this.updateForm(purchaseOrder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrder = this.createFromForm();
    if (purchaseOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.purchaseOrderService.update(purchaseOrder));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderService.create(purchaseOrder));
    }
  }

  trackSupplierById(index: number, item: ISupplier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrder>>): void {
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

  protected updateForm(purchaseOrder: IPurchaseOrder): void {
    this.editForm.patchValue({
      id: purchaseOrder.id,
      status: purchaseOrder.status,
      createDate: purchaseOrder.createDate ? purchaseOrder.createDate.format(DATE_TIME_FORMAT) : null,
      expectedDate: purchaseOrder.expectedDate ? purchaseOrder.expectedDate.format(DATE_TIME_FORMAT) : null,
      paymentDate: purchaseOrder.paymentDate ? purchaseOrder.paymentDate.format(DATE_TIME_FORMAT) : null,
      paymentMethod: purchaseOrder.paymentMethod,
      paymentAmount: purchaseOrder.paymentAmount,
      supplier: purchaseOrder.supplier,
    });

    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing(
      this.suppliersSharedCollection,
      purchaseOrder.supplier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing(suppliers, this.editForm.get('supplier')!.value)
        )
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));
  }

  protected createFromForm(): IPurchaseOrder {
    return {
      ...new PurchaseOrder(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      expectedDate: this.editForm.get(['expectedDate'])!.value
        ? dayjs(this.editForm.get(['expectedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      paymentDate: this.editForm.get(['paymentDate'])!.value
        ? dayjs(this.editForm.get(['paymentDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      paymentMethod: this.editForm.get(['paymentMethod'])!.value,
      paymentAmount: this.editForm.get(['paymentAmount'])!.value,
      supplier: this.editForm.get(['supplier'])!.value,
    };
  }
}
