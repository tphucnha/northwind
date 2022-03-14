import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';
import { OrderItemDeleteDialogComponent } from '../delete/order-item-delete-dialog.component';

@Component({
  selector: 'jhi-order-item',
  templateUrl: './order-item.component.html',
})
export class OrderItemComponent implements OnInit {
  orderItems?: IOrderItem[];
  isLoading = false;

  constructor(protected orderItemService: OrderItemService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.orderItemService.query().subscribe({
      next: (res: HttpResponse<IOrderItem[]>) => {
        this.isLoading = false;
        this.orderItems = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrderItem): number {
    return item.id!;
  }

  delete(orderItem: IOrderItem): void {
    const modalRef = this.modalService.open(OrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderItem = orderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
