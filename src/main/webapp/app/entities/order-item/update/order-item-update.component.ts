import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrderItem, OrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { CustomerOrderService } from 'app/entities/customer-order/service/customer-order.service';
import { OrderItemStatus } from 'app/entities/enumerations/order-item-status.model';

@Component({
  selector: 'jhi-order-item-update',
  templateUrl: './order-item-update.component.html',
})
export class OrderItemUpdateComponent implements OnInit {
  isSaving = false;
  orderItemStatusValues = Object.keys(OrderItemStatus);

  productsCollection: IProduct[] = [];
  customerOrdersSharedCollection: ICustomerOrder[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [],
    unitPrice: [],
    discount: [],
    status: [],
    allocatedDate: [],
    product: [null, Validators.required],
    order: [null, Validators.required],
  });

  constructor(
    protected orderItemService: OrderItemService,
    protected productService: ProductService,
    protected customerOrderService: CustomerOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItem }) => {
      if (orderItem.id === undefined) {
        const today = dayjs().startOf('day');
        orderItem.allocatedDate = today;
      }

      this.updateForm(orderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderItem = this.createFromForm();
    if (orderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.orderItemService.update(orderItem));
    } else {
      this.subscribeToSaveResponse(this.orderItemService.create(orderItem));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackCustomerOrderById(index: number, item: ICustomerOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
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

  protected updateForm(orderItem: IOrderItem): void {
    this.editForm.patchValue({
      id: orderItem.id,
      quantity: orderItem.quantity,
      unitPrice: orderItem.unitPrice,
      discount: orderItem.discount,
      status: orderItem.status,
      allocatedDate: orderItem.allocatedDate ? orderItem.allocatedDate.format(DATE_TIME_FORMAT) : null,
      product: orderItem.product,
      order: orderItem.order,
    });

    this.productsCollection = this.productService.addProductToCollectionIfMissing(this.productsCollection, orderItem.product);
    this.customerOrdersSharedCollection = this.customerOrderService.addCustomerOrderToCollectionIfMissing(
      this.customerOrdersSharedCollection,
      orderItem.order
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query({ filter: 'orderitem-is-null' })
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsCollection = products));

    this.customerOrderService
      .query()
      .pipe(map((res: HttpResponse<ICustomerOrder[]>) => res.body ?? []))
      .pipe(
        map((customerOrders: ICustomerOrder[]) =>
          this.customerOrderService.addCustomerOrderToCollectionIfMissing(customerOrders, this.editForm.get('order')!.value)
        )
      )
      .subscribe((customerOrders: ICustomerOrder[]) => (this.customerOrdersSharedCollection = customerOrders));
  }

  protected createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      unitPrice: this.editForm.get(['unitPrice'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      status: this.editForm.get(['status'])!.value,
      allocatedDate: this.editForm.get(['allocatedDate'])!.value
        ? dayjs(this.editForm.get(['allocatedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      product: this.editForm.get(['product'])!.value,
      order: this.editForm.get(['order'])!.value,
    };
  }
}
