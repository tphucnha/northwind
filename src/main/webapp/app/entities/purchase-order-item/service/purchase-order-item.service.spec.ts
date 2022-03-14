import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPurchaseOrderItem, PurchaseOrderItem } from '../purchase-order-item.model';

import { PurchaseOrderItemService } from './purchase-order-item.service';

describe('PurchaseOrderItem Service', () => {
  let service: PurchaseOrderItemService;
  let httpMock: HttpTestingController;
  let elemDefault: IPurchaseOrderItem;
  let expectedResult: IPurchaseOrderItem | IPurchaseOrderItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PurchaseOrderItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      quantity: 0,
      unitCost: 0,
      receivedDate: currentDate,
      inventoryPosted: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          receivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PurchaseOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          receivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          receivedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PurchaseOrderItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PurchaseOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantity: 1,
          unitCost: 1,
          receivedDate: currentDate.format(DATE_TIME_FORMAT),
          inventoryPosted: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          receivedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PurchaseOrderItem', () => {
      const patchObject = Object.assign(
        {
          unitCost: 1,
          receivedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new PurchaseOrderItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          receivedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PurchaseOrderItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          quantity: 1,
          unitCost: 1,
          receivedDate: currentDate.format(DATE_TIME_FORMAT),
          inventoryPosted: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          receivedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PurchaseOrderItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPurchaseOrderItemToCollectionIfMissing', () => {
      it('should add a PurchaseOrderItem to an empty array', () => {
        const purchaseOrderItem: IPurchaseOrderItem = { id: 123 };
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing([], purchaseOrderItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrderItem);
      });

      it('should not add a PurchaseOrderItem to an array that contains it', () => {
        const purchaseOrderItem: IPurchaseOrderItem = { id: 123 };
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [
          {
            ...purchaseOrderItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(purchaseOrderItemCollection, purchaseOrderItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PurchaseOrderItem to an array that doesn't contain it", () => {
        const purchaseOrderItem: IPurchaseOrderItem = { id: 123 };
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [{ id: 456 }];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(purchaseOrderItemCollection, purchaseOrderItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrderItem);
      });

      it('should add only unique PurchaseOrderItem to an array', () => {
        const purchaseOrderItemArray: IPurchaseOrderItem[] = [{ id: 123 }, { id: 456 }, { id: 15860 }];
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [{ id: 123 }];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(purchaseOrderItemCollection, ...purchaseOrderItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const purchaseOrderItem: IPurchaseOrderItem = { id: 123 };
        const purchaseOrderItem2: IPurchaseOrderItem = { id: 456 };
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing([], purchaseOrderItem, purchaseOrderItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrderItem);
        expect(expectedResult).toContain(purchaseOrderItem2);
      });

      it('should accept null and undefined values', () => {
        const purchaseOrderItem: IPurchaseOrderItem = { id: 123 };
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing([], null, purchaseOrderItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrderItem);
      });

      it('should return initial array if no PurchaseOrderItem is added', () => {
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [{ id: 123 }];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(purchaseOrderItemCollection, undefined, null);
        expect(expectedResult).toEqual(purchaseOrderItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
