import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICustomerOrder } from '../customer-order.model';

@Component({
  selector: 'jhi-customer-order-detail',
  templateUrl: './customer-order-detail.component.html',
})
export class CustomerOrderDetailComponent implements OnInit {
  customerOrder: ICustomerOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerOrder }) => {
      this.customerOrder = customerOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
