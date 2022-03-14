import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchaseOrderItem, getPurchaseOrderItemIdentifier } from '../purchase-order-item.model';

export type EntityResponseType = HttpResponse<IPurchaseOrderItem>;
export type EntityArrayResponseType = HttpResponse<IPurchaseOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchase-order-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchaseOrderItem: IPurchaseOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrderItem);
    return this.http
      .post<IPurchaseOrderItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseOrderItem: IPurchaseOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrderItem);
    return this.http
      .put<IPurchaseOrderItem>(`${this.resourceUrl}/${getPurchaseOrderItemIdentifier(purchaseOrderItem) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchaseOrderItem: IPurchaseOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrderItem);
    return this.http
      .patch<IPurchaseOrderItem>(`${this.resourceUrl}/${getPurchaseOrderItemIdentifier(purchaseOrderItem) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseOrderItemToCollectionIfMissing(
    purchaseOrderItemCollection: IPurchaseOrderItem[],
    ...purchaseOrderItemsToCheck: (IPurchaseOrderItem | null | undefined)[]
  ): IPurchaseOrderItem[] {
    const purchaseOrderItems: IPurchaseOrderItem[] = purchaseOrderItemsToCheck.filter(isPresent);
    if (purchaseOrderItems.length > 0) {
      const purchaseOrderItemCollectionIdentifiers = purchaseOrderItemCollection.map(
        purchaseOrderItemItem => getPurchaseOrderItemIdentifier(purchaseOrderItemItem)!
      );
      const purchaseOrderItemsToAdd = purchaseOrderItems.filter(purchaseOrderItemItem => {
        const purchaseOrderItemIdentifier = getPurchaseOrderItemIdentifier(purchaseOrderItemItem);
        if (purchaseOrderItemIdentifier == null || purchaseOrderItemCollectionIdentifiers.includes(purchaseOrderItemIdentifier)) {
          return false;
        }
        purchaseOrderItemCollectionIdentifiers.push(purchaseOrderItemIdentifier);
        return true;
      });
      return [...purchaseOrderItemsToAdd, ...purchaseOrderItemCollection];
    }
    return purchaseOrderItemCollection;
  }

  protected convertDateFromClient(purchaseOrderItem: IPurchaseOrderItem): IPurchaseOrderItem {
    return Object.assign({}, purchaseOrderItem, {
      receivedDate: purchaseOrderItem.receivedDate?.isValid() ? purchaseOrderItem.receivedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.receivedDate = res.body.receivedDate ? dayjs(res.body.receivedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((purchaseOrderItem: IPurchaseOrderItem) => {
        purchaseOrderItem.receivedDate = purchaseOrderItem.receivedDate ? dayjs(purchaseOrderItem.receivedDate) : undefined;
      });
    }
    return res;
  }
}
