import {Component} from '@angular/core';
import {CustomerOrderComponent} from "../../entities/customer-order/list/customer-order.component";

@Component({
  selector: 'jhi-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent extends CustomerOrderComponent {

}
