import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CustomerOrderService } from '../service/customer-order.service';
import { ICustomerOrder, CustomerOrder } from '../customer-order.model';
import { IInventoryTransaction } from 'app/entities/inventory-transaction/inventory-transaction.model';
import { InventoryTransactionService } from 'app/entities/inventory-transaction/service/inventory-transaction.service';

import { CustomerOrderUpdateComponent } from './customer-order-update.component';

describe('CustomerOrder Management Update Component', () => {
  let comp: CustomerOrderUpdateComponent;
  let fixture: ComponentFixture<CustomerOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerOrderService: CustomerOrderService;
  let inventoryTransactionService: InventoryTransactionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CustomerOrderUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CustomerOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerOrderService = TestBed.inject(CustomerOrderService);
    inventoryTransactionService = TestBed.inject(InventoryTransactionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InventoryTransaction query and add missing value', () => {
      const customerOrder: ICustomerOrder = { id: 456 };
      const inventoryTransaction: IInventoryTransaction = { id: 12820 };
      customerOrder.inventoryTransaction = inventoryTransaction;

      const inventoryTransactionCollection: IInventoryTransaction[] = [{ id: 15837 }];
      jest.spyOn(inventoryTransactionService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryTransactionCollection })));
      const additionalInventoryTransactions = [inventoryTransaction];
      const expectedCollection: IInventoryTransaction[] = [...additionalInventoryTransactions, ...inventoryTransactionCollection];
      jest.spyOn(inventoryTransactionService, 'addInventoryTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      expect(inventoryTransactionService.query).toHaveBeenCalled();
      expect(inventoryTransactionService.addInventoryTransactionToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryTransactionCollection,
        ...additionalInventoryTransactions
      );
      expect(comp.inventoryTransactionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const customerOrder: ICustomerOrder = { id: 456 };
      const inventoryTransaction: IInventoryTransaction = { id: 32342 };
      customerOrder.inventoryTransaction = inventoryTransaction;

      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(customerOrder));
      expect(comp.inventoryTransactionsSharedCollection).toContain(inventoryTransaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerOrder>>();
      const customerOrder = { id: 123 };
      jest.spyOn(customerOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerOrder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerOrderService.update).toHaveBeenCalledWith(customerOrder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerOrder>>();
      const customerOrder = new CustomerOrder();
      jest.spyOn(customerOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerOrder }));
      saveSubject.complete();

      // THEN
      expect(customerOrderService.create).toHaveBeenCalledWith(customerOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CustomerOrder>>();
      const customerOrder = { id: 123 };
      jest.spyOn(customerOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerOrderService.update).toHaveBeenCalledWith(customerOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInventoryTransactionById', () => {
      it('Should return tracked InventoryTransaction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInventoryTransactionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
