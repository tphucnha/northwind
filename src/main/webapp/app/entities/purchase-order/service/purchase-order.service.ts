import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchaseOrder, getPurchaseOrderIdentifier } from '../purchase-order.model';

export type EntityResponseType = HttpResponse<IPurchaseOrder>;
export type EntityArrayResponseType = HttpResponse<IPurchaseOrder[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchase-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .post<IPurchaseOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .put<IPurchaseOrder>(`${this.resourceUrl}/${getPurchaseOrderIdentifier(purchaseOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(purchaseOrder: IPurchaseOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(purchaseOrder);
    return this.http
      .patch<IPurchaseOrder>(`${this.resourceUrl}/${getPurchaseOrderIdentifier(purchaseOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPurchaseOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPurchaseOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPurchaseOrderToCollectionIfMissing(
    purchaseOrderCollection: IPurchaseOrder[],
    ...purchaseOrdersToCheck: (IPurchaseOrder | null | undefined)[]
  ): IPurchaseOrder[] {
    const purchaseOrders: IPurchaseOrder[] = purchaseOrdersToCheck.filter(isPresent);
    if (purchaseOrders.length > 0) {
      const purchaseOrderCollectionIdentifiers = purchaseOrderCollection.map(
        purchaseOrderItem => getPurchaseOrderIdentifier(purchaseOrderItem)!
      );
      const purchaseOrdersToAdd = purchaseOrders.filter(purchaseOrderItem => {
        const purchaseOrderIdentifier = getPurchaseOrderIdentifier(purchaseOrderItem);
        if (purchaseOrderIdentifier == null || purchaseOrderCollectionIdentifiers.includes(purchaseOrderIdentifier)) {
          return false;
        }
        purchaseOrderCollectionIdentifiers.push(purchaseOrderIdentifier);
        return true;
      });
      return [...purchaseOrdersToAdd, ...purchaseOrderCollection];
    }
    return purchaseOrderCollection;
  }

  protected convertDateFromClient(purchaseOrder: IPurchaseOrder): IPurchaseOrder {
    return Object.assign({}, purchaseOrder, {
      createDate: purchaseOrder.createDate?.isValid() ? purchaseOrder.createDate.toJSON() : undefined,
      expectedDate: purchaseOrder.expectedDate?.isValid() ? purchaseOrder.expectedDate.toJSON() : undefined,
      paymentDate: purchaseOrder.paymentDate?.isValid() ? purchaseOrder.paymentDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
      res.body.expectedDate = res.body.expectedDate ? dayjs(res.body.expectedDate) : undefined;
      res.body.paymentDate = res.body.paymentDate ? dayjs(res.body.paymentDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((purchaseOrder: IPurchaseOrder) => {
        purchaseOrder.createDate = purchaseOrder.createDate ? dayjs(purchaseOrder.createDate) : undefined;
        purchaseOrder.expectedDate = purchaseOrder.expectedDate ? dayjs(purchaseOrder.expectedDate) : undefined;
        purchaseOrder.paymentDate = purchaseOrder.paymentDate ? dayjs(purchaseOrder.paymentDate) : undefined;
      });
    }
    return res;
  }
}
