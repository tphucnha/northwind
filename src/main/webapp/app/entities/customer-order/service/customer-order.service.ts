import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICustomerOrder, getCustomerOrderIdentifier } from '../customer-order.model';

export type EntityResponseType = HttpResponse<ICustomerOrder>;
export type EntityArrayResponseType = HttpResponse<ICustomerOrder[]>;

@Injectable({ providedIn: 'root' })
export class CustomerOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/customer-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customerOrder: ICustomerOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerOrder);
    return this.http
      .post<ICustomerOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(customerOrder: ICustomerOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerOrder);
    return this.http
      .put<ICustomerOrder>(`${this.resourceUrl}/${getCustomerOrderIdentifier(customerOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(customerOrder: ICustomerOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(customerOrder);
    return this.http
      .patch<ICustomerOrder>(`${this.resourceUrl}/${getCustomerOrderIdentifier(customerOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICustomerOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICustomerOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCustomerOrderToCollectionIfMissing(
    customerOrderCollection: ICustomerOrder[],
    ...customerOrdersToCheck: (ICustomerOrder | null | undefined)[]
  ): ICustomerOrder[] {
    const customerOrders: ICustomerOrder[] = customerOrdersToCheck.filter(isPresent);
    if (customerOrders.length > 0) {
      const customerOrderCollectionIdentifiers = customerOrderCollection.map(
        customerOrderItem => getCustomerOrderIdentifier(customerOrderItem)!
      );
      const customerOrdersToAdd = customerOrders.filter(customerOrderItem => {
        const customerOrderIdentifier = getCustomerOrderIdentifier(customerOrderItem);
        if (customerOrderIdentifier == null || customerOrderCollectionIdentifiers.includes(customerOrderIdentifier)) {
          return false;
        }
        customerOrderCollectionIdentifiers.push(customerOrderIdentifier);
        return true;
      });
      return [...customerOrdersToAdd, ...customerOrderCollection];
    }
    return customerOrderCollection;
  }

  protected convertDateFromClient(customerOrder: ICustomerOrder): ICustomerOrder {
    return Object.assign({}, customerOrder, {
      orderDate: customerOrder.orderDate?.isValid() ? customerOrder.orderDate.toJSON() : undefined,
      shippedDate: customerOrder.shippedDate?.isValid() ? customerOrder.shippedDate.toJSON() : undefined,
      paidDate: customerOrder.paidDate?.isValid() ? customerOrder.paidDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.orderDate = res.body.orderDate ? dayjs(res.body.orderDate) : undefined;
      res.body.shippedDate = res.body.shippedDate ? dayjs(res.body.shippedDate) : undefined;
      res.body.paidDate = res.body.paidDate ? dayjs(res.body.paidDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((customerOrder: ICustomerOrder) => {
        customerOrder.orderDate = customerOrder.orderDate ? dayjs(customerOrder.orderDate) : undefined;
        customerOrder.shippedDate = customerOrder.shippedDate ? dayjs(customerOrder.shippedDate) : undefined;
        customerOrder.paidDate = customerOrder.paidDate ? dayjs(customerOrder.paidDate) : undefined;
      });
    }
    return res;
  }
}
