import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICustomerOrder, CustomerOrder } from '../customer-order.model';
import { CustomerOrderService } from '../service/customer-order.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

@Component({
  selector: 'jhi-customer-order-update',
  templateUrl: './customer-order-update.component.html',
})
export class CustomerOrderUpdateComponent implements OnInit {
  isSaving = false;
  orderStatusValues = Object.keys(OrderStatus);

  customersSharedCollection: ICustomer[] = [];

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
    customer: [null, Validators.required],
  });

  constructor(
    protected customerOrderService: CustomerOrderService,
    protected customerService: CustomerService,
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

  trackCustomerById(index: number, item: ICustomer): number {
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
      customer: customerOrder.customer,
    });

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(
      this.customersSharedCollection,
      customerOrder.customer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing(customers, this.editForm.get('customer')!.value)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
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
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
