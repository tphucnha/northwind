import {Component, OnInit} from '@angular/core';
import {CustomerOrderDetailComponent} from "../../entities/customer-order/detail/customer-order-detail.component";

@Component({
  selector: 'jhi-order-detail',
  templateUrl: './order-detail.component.html',
  styleUrls: ['./order-detail.component.scss']
})
export class OrderDetailComponent extends CustomerOrderDetailComponent {

}
