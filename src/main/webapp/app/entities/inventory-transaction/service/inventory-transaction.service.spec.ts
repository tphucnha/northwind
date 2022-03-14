import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { InventoryTransactionType } from 'app/entities/enumerations/inventory-transaction-type.model';
import { IInventoryTransaction, InventoryTransaction } from '../inventory-transaction.model';

import { InventoryTransactionService } from './inventory-transaction.service';

describe('InventoryTransaction Service', () => {
  let service: InventoryTransactionService;
  let httpMock: HttpTestingController;
  let elemDefault: IInventoryTransaction;
  let expectedResult: IInventoryTransaction | IInventoryTransaction[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InventoryTransactionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      transactionType: InventoryTransactionType.PURCHASED,
      createDate: currentDate,
      modifiedDate: currentDate,
      quantity: 0,
      comments: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createDate: currentDate.format(DATE_TIME_FORMAT),
          modifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a InventoryTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          modifiedDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.create(new InventoryTransaction()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InventoryTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionType: 'BBBBBB',
          createDate: currentDate.format(DATE_TIME_FORMAT),
          modifiedDate: currentDate.format(DATE_TIME_FORMAT),
          quantity: 1,
          comments: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InventoryTransaction', () => {
      const patchObject = Object.assign(
        {
          transactionType: 'BBBBBB',
          modifiedDate: currentDate.format(DATE_TIME_FORMAT),
          quantity: 1,
        },
        new InventoryTransaction()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InventoryTransaction', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          transactionType: 'BBBBBB',
          createDate: currentDate.format(DATE_TIME_FORMAT),
          modifiedDate: currentDate.format(DATE_TIME_FORMAT),
          quantity: 1,
          comments: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
          modifiedDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a InventoryTransaction', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInventoryTransactionToCollectionIfMissing', () => {
      it('should add a InventoryTransaction to an empty array', () => {
        const inventoryTransaction: IInventoryTransaction = { id: 123 };
        expectedResult = service.addInventoryTransactionToCollectionIfMissing([], inventoryTransaction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventoryTransaction);
      });

      it('should not add a InventoryTransaction to an array that contains it', () => {
        const inventoryTransaction: IInventoryTransaction = { id: 123 };
        const inventoryTransactionCollection: IInventoryTransaction[] = [
          {
            ...inventoryTransaction,
          },
          { id: 456 },
        ];
        expectedResult = service.addInventoryTransactionToCollectionIfMissing(inventoryTransactionCollection, inventoryTransaction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InventoryTransaction to an array that doesn't contain it", () => {
        const inventoryTransaction: IInventoryTransaction = { id: 123 };
        const inventoryTransactionCollection: IInventoryTransaction[] = [{ id: 456 }];
        expectedResult = service.addInventoryTransactionToCollectionIfMissing(inventoryTransactionCollection, inventoryTransaction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryTransaction);
      });

      it('should add only unique InventoryTransaction to an array', () => {
        const inventoryTransactionArray: IInventoryTransaction[] = [{ id: 123 }, { id: 456 }, { id: 96268 }];
        const inventoryTransactionCollection: IInventoryTransaction[] = [{ id: 123 }];
        expectedResult = service.addInventoryTransactionToCollectionIfMissing(inventoryTransactionCollection, ...inventoryTransactionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventoryTransaction: IInventoryTransaction = { id: 123 };
        const inventoryTransaction2: IInventoryTransaction = { id: 456 };
        expectedResult = service.addInventoryTransactionToCollectionIfMissing([], inventoryTransaction, inventoryTransaction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventoryTransaction);
        expect(expectedResult).toContain(inventoryTransaction2);
      });

      it('should accept null and undefined values', () => {
        const inventoryTransaction: IInventoryTransaction = { id: 123 };
        expectedResult = service.addInventoryTransactionToCollectionIfMissing([], null, inventoryTransaction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inventoryTransaction);
      });

      it('should return initial array if no InventoryTransaction is added', () => {
        const inventoryTransactionCollection: IInventoryTransaction[] = [{ id: 123 }];
        expectedResult = service.addInventoryTransactionToCollectionIfMissing(inventoryTransactionCollection, undefined, null);
        expect(expectedResult).toEqual(inventoryTransactionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
