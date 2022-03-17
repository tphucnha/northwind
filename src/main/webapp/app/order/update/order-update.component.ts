import {Component, OnInit} from '@angular/core';
import {CustomerOrderUpdateComponent} from "../../entities/customer-order/update/customer-order-update.component";

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
  styleUrls: ['./order-update.component.scss']
})
export class OrderUpdateComponent extends CustomerOrderUpdateComponent implements OnInit {

}
