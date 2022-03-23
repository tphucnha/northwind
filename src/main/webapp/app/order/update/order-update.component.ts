import {Component, ViewChild} from '@angular/core';
import {CustomerOrderUpdateComponent} from "../../entities/customer-order/update/customer-order-update.component";
import {filter, map} from "rxjs/operators";
import {ICustomer} from "../../entities/customer/customer.model";
import {debounceTime, distinctUntilChanged, merge, Observable, OperatorFunction, Subject} from 'rxjs';
import {NgbTypeahead} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
  styleUrls: ['./order-update.component.scss']
})
export class OrderUpdateComponent extends CustomerOrderUpdateComponent {

  model: any;
  @ViewChild('instance', {static: true}) instance!: NgbTypeahead;
  focus$ = new Subject<string>();
  click$ = new Subject<string>();


  search: OperatorFunction<string, readonly ICustomer[]> = (text$: Observable<string>) => {
    const debouncedText$ = text$.pipe(debounceTime(200), distinctUntilChanged());
    const clicksWithClosedPopup$ = this.click$.pipe(filter(() => !this.instance.isPopupOpen()));
    const inputFocus$ = this.focus$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      map(term => (term === '' ? this.customersSharedCollection
        : this.customersSharedCollection
          .filter(c => {
            const name = `${c.firstName ?? ''} ${c.lastName ?? ''}`;
            if (!name) {
              return false;
            }
            return name.toLowerCase().indexOf(term.toLowerCase()) > -1
          })).slice(0, 10))
    );
  }

  formatter: any = (customer: ICustomer) => `${customer.firstName ?? ''} ${customer.lastName ?? ''}`.trim();

}
