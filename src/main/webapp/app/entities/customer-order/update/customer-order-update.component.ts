import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICustomerOrder, CustomerOrder } from '../customer-order.model';
import { CustomerOrderService } from '../service/customer-order.service';
import { IInventoryTransaction } from 'app/entities/inventory-transaction/inventory-transaction.model';
import { InventoryTransactionService } from 'app/entities/inventory-transaction/service/inventory-transaction.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

@Component({
  selector: 'jhi-customer-order-update',
  templateUrl: './customer-order-update.component.html',
})
export class CustomerOrderUpdateComponent implements OnInit {
  isSaving = false;
  orderStatusValues = Object.keys(OrderStatus);

  inventoryTransactionsSharedCollection: IInventoryTransaction[] = [];

  editForm = this.fb.group({
    id: [],
    orderDate: [],
    shippedDate: [],
    shipAddress: [],
    shippingFee: [],
    taxes: [],
    paymentMethod: [],
    paidDate: [],
    status: [],
    notes: [],
    inventoryTransaction: [],
  });

  constructor(
    protected customerOrderService: CustomerOrderService,
    protected inventoryTransactionService: InventoryTransactionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerOrder }) => {
      if (customerOrder.id === undefined) {
        const today = dayjs().startOf('day');
        customerOrder.orderDate = today;
        customerOrder.shippedDate = today;
        customerOrder.paidDate = today;
      }

      this.updateForm(customerOrder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerOrder = this.createFromForm();
    if (customerOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.customerOrderService.update(customerOrder));
    } else {
      this.subscribeToSaveResponse(this.customerOrderService.create(customerOrder));
    }
  }

  trackInventoryTransactionById(index: number, item: IInventoryTransaction): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerOrder>>): void {
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

  protected updateForm(customerOrder: ICustomerOrder): void {
    this.editForm.patchValue({
      id: customerOrder.id,
      orderDate: customerOrder.orderDate ? customerOrder.orderDate.format(DATE_TIME_FORMAT) : null,
      shippedDate: customerOrder.shippedDate ? customerOrder.shippedDate.format(DATE_TIME_FORMAT) : null,
      shipAddress: customerOrder.shipAddress,
      shippingFee: customerOrder.shippingFee,
      taxes: customerOrder.taxes,
      paymentMethod: customerOrder.paymentMethod,
      paidDate: customerOrder.paidDate ? customerOrder.paidDate.format(DATE_TIME_FORMAT) : null,
      status: customerOrder.status,
      notes: customerOrder.notes,
      inventoryTransaction: customerOrder.inventoryTransaction,
    });

    this.inventoryTransactionsSharedCollection = this.inventoryTransactionService.addInventoryTransactionToCollectionIfMissing(
      this.inventoryTransactionsSharedCollection,
      customerOrder.inventoryTransaction
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryTransactionService
      .query()
      .pipe(map((res: HttpResponse<IInventoryTransaction[]>) => res.body ?? []))
      .pipe(
        map((inventoryTransactions: IInventoryTransaction[]) =>
          this.inventoryTransactionService.addInventoryTransactionToCollectionIfMissing(
            inventoryTransactions,
            this.editForm.get('inventoryTransaction')!.value
          )
        )
      )
      .subscribe((inventoryTransactions: IInventoryTransaction[]) => (this.inventoryTransactionsSharedCollection = inventoryTransactions));
  }

  protected createFromForm(): ICustomerOrder {
    return {
      ...new CustomerOrder(),
      id: this.editForm.get(['id'])!.value,
      orderDate: this.editForm.get(['orderDate'])!.value ? dayjs(this.editForm.get(['orderDate'])!.value, DATE_TIME_FORMAT) : undefined,
      shippedDate: this.editForm.get(['shippedDate'])!.value
        ? dayjs(this.editForm.get(['shippedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      shipAddress: this.editForm.get(['shipAddress'])!.value,
      shippingFee: this.editForm.get(['shippingFee'])!.value,
      taxes: this.editForm.get(['taxes'])!.value,
      paymentMethod: this.editForm.get(['paymentMethod'])!.value,
      paidDate: this.editForm.get(['paidDate'])!.value ? dayjs(this.editForm.get(['paidDate'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      inventoryTransaction: this.editForm.get(['inventoryTransaction'])!.value,
    };
  }
}
