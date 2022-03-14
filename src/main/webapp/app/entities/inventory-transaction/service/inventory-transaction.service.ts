import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInventoryTransaction, getInventoryTransactionIdentifier } from '../inventory-transaction.model';

export type EntityResponseType = HttpResponse<IInventoryTransaction>;
export type EntityArrayResponseType = HttpResponse<IInventoryTransaction[]>;

@Injectable({ providedIn: 'root' })
export class InventoryTransactionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inventory-transactions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(inventoryTransaction: IInventoryTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventoryTransaction);
    return this.http
      .post<IInventoryTransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(inventoryTransaction: IInventoryTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventoryTransaction);
    return this.http
      .put<IInventoryTransaction>(`${this.resourceUrl}/${getInventoryTransactionIdentifier(inventoryTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(inventoryTransaction: IInventoryTransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inventoryTransaction);
    return this.http
      .patch<IInventoryTransaction>(`${this.resourceUrl}/${getInventoryTransactionIdentifier(inventoryTransaction) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInventoryTransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInventoryTransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInventoryTransactionToCollectionIfMissing(
    inventoryTransactionCollection: IInventoryTransaction[],
    ...inventoryTransactionsToCheck: (IInventoryTransaction | null | undefined)[]
  ): IInventoryTransaction[] {
    const inventoryTransactions: IInventoryTransaction[] = inventoryTransactionsToCheck.filter(isPresent);
    if (inventoryTransactions.length > 0) {
      const inventoryTransactionCollectionIdentifiers = inventoryTransactionCollection.map(
        inventoryTransactionItem => getInventoryTransactionIdentifier(inventoryTransactionItem)!
      );
      const inventoryTransactionsToAdd = inventoryTransactions.filter(inventoryTransactionItem => {
        const inventoryTransactionIdentifier = getInventoryTransactionIdentifier(inventoryTransactionItem);
        if (inventoryTransactionIdentifier == null || inventoryTransactionCollectionIdentifiers.includes(inventoryTransactionIdentifier)) {
          return false;
        }
        inventoryTransactionCollectionIdentifiers.push(inventoryTransactionIdentifier);
        return true;
      });
      return [...inventoryTransactionsToAdd, ...inventoryTransactionCollection];
    }
    return inventoryTransactionCollection;
  }

  protected convertDateFromClient(inventoryTransaction: IInventoryTransaction): IInventoryTransaction {
    return Object.assign({}, inventoryTransaction, {
      createDate: inventoryTransaction.createDate?.isValid() ? inventoryTransaction.createDate.toJSON() : undefined,
      modifiedDate: inventoryTransaction.modifiedDate?.isValid() ? inventoryTransaction.modifiedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.modifiedDate = res.body.modifiedDate ? dayjs(res.body.modifiedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((inventoryTransaction: IInventoryTransaction) => {
        inventoryTransaction.createDate = inventoryTransaction.createDate ? dayjs(inventoryTransaction.createDate) : undefined;
        inventoryTransaction.modifiedDate = inventoryTransaction.modifiedDate ? dayjs(inventoryTransaction.modifiedDate) : undefined;
      });
    }
    return res;
  }
}
