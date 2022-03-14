import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPurchaseOrderItem, PurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';

@Injectable({ providedIn: 'root' })
export class PurchaseOrderItemRoutingResolveService implements Resolve<IPurchaseOrderItem> {
  constructor(protected service: PurchaseOrderItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPurchaseOrderItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((purchaseOrderItem: HttpResponse<PurchaseOrderItem>) => {
          if (purchaseOrderItem.body) {
            return of(purchaseOrderItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PurchaseOrderItem());
  }
}
