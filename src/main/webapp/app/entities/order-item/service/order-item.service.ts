import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderItem, getOrderItemIdentifier } from '../order-item.model';

export type EntityResponseType = HttpResponse<IOrderItem>;
export type EntityArrayResponseType = HttpResponse<IOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class OrderItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderItem: IOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .post<IOrderItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(orderItem: IOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .put<IOrderItem>(`${this.resourceUrl}/${getOrderItemIdentifier(orderItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(orderItem: IOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .patch<IOrderItem>(`${this.resourceUrl}/${getOrderItemIdentifier(orderItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrderItemToCollectionIfMissing(
    orderItemCollection: IOrderItem[],
    ...orderItemsToCheck: (IOrderItem | null | undefined)[]
  ): IOrderItem[] {
    const orderItems: IOrderItem[] = orderItemsToCheck.filter(isPresent);
    if (orderItems.length > 0) {
      const orderItemCollectionIdentifiers = orderItemCollection.map(orderItemItem => getOrderItemIdentifier(orderItemItem)!);
      const orderItemsToAdd = orderItems.filter(orderItemItem => {
        const orderItemIdentifier = getOrderItemIdentifier(orderItemItem);
        if (orderItemIdentifier == null || orderItemCollectionIdentifiers.includes(orderItemIdentifier)) {
          return false;
        }
        orderItemCollectionIdentifiers.push(orderItemIdentifier);
        return true;
      });
      return [...orderItemsToAdd, ...orderItemCollection];
    }
    return orderItemCollection;
  }

  protected convertDateFromClient(orderItem: IOrderItem): IOrderItem {
    return Object.assign({}, orderItem, {
      allocatedDate: orderItem.allocatedDate?.isValid() ? orderItem.allocatedDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.allocatedDate = res.body.allocatedDate ? dayjs(res.body.allocatedDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((orderItem: IOrderItem) => {
        orderItem.allocatedDate = orderItem.allocatedDate ? dayjs(orderItem.allocatedDate) : undefined;
      });
    }
    return res;
  }
}
