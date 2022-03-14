import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInventoryTransaction, InventoryTransaction } from '../inventory-transaction.model';
import { InventoryTransactionService } from '../service/inventory-transaction.service';

@Injectable({ providedIn: 'root' })
export class InventoryTransactionRoutingResolveService implements Resolve<IInventoryTransaction> {
  constructor(protected service: InventoryTransactionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInventoryTransaction> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((inventoryTransaction: HttpResponse<InventoryTransaction>) => {
          if (inventoryTransaction.body) {
            return of(inventoryTransaction.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InventoryTransaction());
  }
}
