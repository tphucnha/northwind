import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PurchaseOrderService } from '../service/purchase-order.service';
import { IPurchaseOrder, PurchaseOrder } from '../purchase-order.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';
import { IInventoryTransaction } from 'app/entities/inventory-transaction/inventory-transaction.model';
import { InventoryTransactionService } from 'app/entities/inventory-transaction/service/inventory-transaction.service';

import { PurchaseOrderUpdateComponent } from './purchase-order-update.component';

describe('PurchaseOrder Management Update Component', () => {
  let comp: PurchaseOrderUpdateComponent;
  let fixture: ComponentFixture<PurchaseOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseOrderService: PurchaseOrderService;
  let supplierService: SupplierService;
  let inventoryTransactionService: InventoryTransactionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PurchaseOrderUpdateComponent],
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
      .overrideTemplate(PurchaseOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    supplierService = TestBed.inject(SupplierService);
    inventoryTransactionService = TestBed.inject(InventoryTransactionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Supplier query and add missing value', () => {
      const purchaseOrder: IPurchaseOrder = { id: 456 };
      const supplier: ISupplier = { id: 61649 };
      purchaseOrder.supplier = supplier;

      const supplierCollection: ISupplier[] = [{ id: 61359 }];
      jest.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [supplier];
      const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
      jest.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(supplierCollection, ...additionalSuppliers);
      expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InventoryTransaction query and add missing value', () => {
      const purchaseOrder: IPurchaseOrder = { id: 456 };
      const inventoryTransaction: IInventoryTransaction = { id: 91816 };
      purchaseOrder.inventoryTransaction = inventoryTransaction;

      const inventoryTransactionCollection: IInventoryTransaction[] = [{ id: 47015 }];
      jest.spyOn(inventoryTransactionService, 'query').mockReturnValue(of(new HttpResponse({ body: inventoryTransactionCollection })));
      const additionalInventoryTransactions = [inventoryTransaction];
      const expectedCollection: IInventoryTransaction[] = [...additionalInventoryTransactions, ...inventoryTransactionCollection];
      jest.spyOn(inventoryTransactionService, 'addInventoryTransactionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(inventoryTransactionService.query).toHaveBeenCalled();
      expect(inventoryTransactionService.addInventoryTransactionToCollectionIfMissing).toHaveBeenCalledWith(
        inventoryTransactionCollection,
        ...additionalInventoryTransactions
      );
      expect(comp.inventoryTransactionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchaseOrder: IPurchaseOrder = { id: 456 };
      const supplier: ISupplier = { id: 25227 };
      purchaseOrder.supplier = supplier;
      const inventoryTransaction: IInventoryTransaction = { id: 69106 };
      purchaseOrder.inventoryTransaction = inventoryTransaction;

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(purchaseOrder));
      expect(comp.suppliersSharedCollection).toContain(supplier);
      expect(comp.inventoryTransactionsSharedCollection).toContain(inventoryTransaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrder>>();
      const purchaseOrder = { id: 123 };
      jest.spyOn(purchaseOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrder }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseOrderService.update).toHaveBeenCalledWith(purchaseOrder);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrder>>();
      const purchaseOrder = new PurchaseOrder();
      jest.spyOn(purchaseOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrder }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderService.create).toHaveBeenCalledWith(purchaseOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrder>>();
      const purchaseOrder = { id: 123 };
      jest.spyOn(purchaseOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchaseOrderService.update).toHaveBeenCalledWith(purchaseOrder);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSupplierById', () => {
      it('Should return tracked Supplier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSupplierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInventoryTransactionById', () => {
      it('Should return tracked InventoryTransaction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInventoryTransactionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
