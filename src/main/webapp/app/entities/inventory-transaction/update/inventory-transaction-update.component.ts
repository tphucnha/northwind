import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInventoryTransaction, InventoryTransaction } from '../inventory-transaction.model';
import { InventoryTransactionService } from '../service/inventory-transaction.service';
import { InventoryTransactionType } from 'app/entities/enumerations/inventory-transaction-type.model';

@Component({
  selector: 'jhi-inventory-transaction-update',
  templateUrl: './inventory-transaction-update.component.html',
})
export class InventoryTransactionUpdateComponent implements OnInit {
  isSaving = false;
  inventoryTransactionTypeValues = Object.keys(InventoryTransactionType);

  editForm = this.fb.group({
    id: [],
    transactionType: [],
    createDate: [],
    modifiedDate: [],
    quantity: [],
    comments: [],
  });

  constructor(
    protected inventoryTransactionService: InventoryTransactionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryTransaction }) => {
      if (inventoryTransaction.id === undefined) {
        const today = dayjs().startOf('day');
        inventoryTransaction.createDate = today;
        inventoryTransaction.modifiedDate = today;
      }

      this.updateForm(inventoryTransaction);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inventoryTransaction = this.createFromForm();
    if (inventoryTransaction.id !== undefined) {
      this.subscribeToSaveResponse(this.inventoryTransactionService.update(inventoryTransaction));
    } else {
      this.subscribeToSaveResponse(this.inventoryTransactionService.create(inventoryTransaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInventoryTransaction>>): void {
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

  protected updateForm(inventoryTransaction: IInventoryTransaction): void {
    this.editForm.patchValue({
      id: inventoryTransaction.id,
      transactionType: inventoryTransaction.transactionType,
      createDate: inventoryTransaction.createDate ? inventoryTransaction.createDate.format(DATE_TIME_FORMAT) : null,
      modifiedDate: inventoryTransaction.modifiedDate ? inventoryTransaction.modifiedDate.format(DATE_TIME_FORMAT) : null,
      quantity: inventoryTransaction.quantity,
      comments: inventoryTransaction.comments,
    });
  }

  protected createFromForm(): IInventoryTransaction {
    return {
      ...new InventoryTransaction(),
      id: this.editForm.get(['id'])!.value,
      transactionType: this.editForm.get(['transactionType'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      modifiedDate: this.editForm.get(['modifiedDate'])!.value
        ? dayjs(this.editForm.get(['modifiedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      quantity: this.editForm.get(['quantity'])!.value,
      comments: this.editForm.get(['comments'])!.value,
    };
  }
}
