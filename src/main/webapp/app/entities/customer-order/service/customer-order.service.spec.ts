import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { ICustomerOrder, CustomerOrder } from '../customer-order.model';

import { CustomerOrderService } from './customer-order.service';

describe('CustomerOrder Service', () => {
  let service: CustomerOrderService;
  let httpMock: HttpTestingController;
  let elemDefault: ICustomerOrder;
  let expectedResult: ICustomerOrder | ICustomerOrder[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CustomerOrderService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      orderDate: currentDate,
      shippedDate: currentDate,
      shipAddress: 'AAAAAAA',
      shippingFee: 0,
      taxes: 0,
      paymentMethod: 'AAAAAAA',
      paidDate: currentDate,
      status: OrderStatus.NEW,
      notes: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          orderDate: currentDate.format(DATE_TIME_FORMAT),
          shippedDate: currentDate.format(DATE_TIME_FORMAT),
          paidDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CustomerOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          orderDate: currentDate.format(DATE_TIME_FORMAT),
          shippedDate: currentDate.format(DATE_TIME_FORMAT),
          paidDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          orderDate: currentDate,
          shippedDate: currentDate,
          paidDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CustomerOrder()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CustomerOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orderDate: currentDate.format(DATE_TIME_FORMAT),
          shippedDate: currentDate.format(DATE_TIME_FORMAT),
          shipAddress: 'BBBBBB',
          shippingFee: 1,
          taxes: 1,
          paymentMethod: 'BBBBBB',
          paidDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          notes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          orderDate: currentDate,
          shippedDate: currentDate,
          paidDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CustomerOrder', () => {
      const patchObject = Object.assign(
        {
          shippedDate: currentDate.format(DATE_TIME_FORMAT),
          shipAddress: 'BBBBBB',
          taxes: 1,
          paymentMethod: 'BBBBBB',
          paidDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
        },
        new CustomerOrder()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          orderDate: currentDate,
          shippedDate: currentDate,
          paidDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CustomerOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orderDate: currentDate.format(DATE_TIME_FORMAT),
          shippedDate: currentDate.format(DATE_TIME_FORMAT),
          shipAddress: 'BBBBBB',
          shippingFee: 1,
          taxes: 1,
          paymentMethod: 'BBBBBB',
          paidDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          notes: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          orderDate: currentDate,
          shippedDate: currentDate,
          paidDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CustomerOrder', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCustomerOrderToCollectionIfMissing', () => {
      it('should add a CustomerOrder to an empty array', () => {
        const customerOrder: ICustomerOrder = { id: 123 };
        expectedResult = service.addCustomerOrderToCollectionIfMissing([], customerOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerOrder);
      });

      it('should not add a CustomerOrder to an array that contains it', () => {
        const customerOrder: ICustomerOrder = { id: 123 };
        const customerOrderCollection: ICustomerOrder[] = [
          {
            ...customerOrder,
          },
          { id: 456 },
        ];
        expectedResult = service.addCustomerOrderToCollectionIfMissing(customerOrderCollection, customerOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CustomerOrder to an array that doesn't contain it", () => {
        const customerOrder: ICustomerOrder = { id: 123 };
        const customerOrderCollection: ICustomerOrder[] = [{ id: 456 }];
        expectedResult = service.addCustomerOrderToCollectionIfMissing(customerOrderCollection, customerOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerOrder);
      });

      it('should add only unique CustomerOrder to an array', () => {
        const customerOrderArray: ICustomerOrder[] = [{ id: 123 }, { id: 456 }, { id: 47856 }];
        const customerOrderCollection: ICustomerOrder[] = [{ id: 123 }];
        expectedResult = service.addCustomerOrderToCollectionIfMissing(customerOrderCollection, ...customerOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const customerOrder: ICustomerOrder = { id: 123 };
        const customerOrder2: ICustomerOrder = { id: 456 };
        expectedResult = service.addCustomerOrderToCollectionIfMissing([], customerOrder, customerOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(customerOrder);
        expect(expectedResult).toContain(customerOrder2);
      });

      it('should accept null and undefined values', () => {
        const customerOrder: ICustomerOrder = { id: 123 };
        expectedResult = service.addCustomerOrderToCollectionIfMissing([], null, customerOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(customerOrder);
      });

      it('should return initial array if no CustomerOrder is added', () => {
        const customerOrderCollection: ICustomerOrder[] = [{ id: 123 }];
        expectedResult = service.addCustomerOrderToCollectionIfMissing(customerOrderCollection, undefined, null);
        expect(expectedResult).toEqual(customerOrderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
