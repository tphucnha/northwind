import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICustomerOrder, CustomerOrder } from '../customer-order.model';
import { CustomerOrderService } from '../service/customer-order.service';

@Injectable({ providedIn: 'root' })
export class CustomerOrderRoutingResolveService implements Resolve<ICustomerOrder> {
  constructor(protected service: CustomerOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICustomerOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((customerOrder: HttpResponse<CustomerOrder>) => {
          if (customerOrder.body) {
            return of(customerOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CustomerOrder());
  }
}
