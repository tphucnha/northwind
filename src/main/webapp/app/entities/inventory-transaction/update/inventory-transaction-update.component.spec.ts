import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InventoryTransactionService } from '../service/inventory-transaction.service';
import { IInventoryTransaction, InventoryTransaction } from '../inventory-transaction.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { InventoryTransactionUpdateComponent } from './inventory-transaction-update.component';

describe('InventoryTransaction Management Update Component', () => {
  let comp: InventoryTransactionUpdateComponent;
  let fixture: ComponentFixture<InventoryTransactionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inventoryTransactionService: InventoryTransactionService;
  let productService: ProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InventoryTransactionUpdateComponent],
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
      .overrideTemplate(InventoryTransactionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InventoryTransactionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventoryTransactionService = TestBed.inject(InventoryTransactionService);
    productService = TestBed.inject(ProductService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call product query and add missing value', () => {
      const inventoryTransaction: IInventoryTransaction = { id: 456 };
      const product: IProduct = { id: 74455 };
      inventoryTransaction.product = product;

      const productCollection: IProduct[] = [{ id: 58069 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const expectedCollection: IProduct[] = [product, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, product);
      expect(comp.productsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inventoryTransaction: IInventoryTransaction = { id: 456 };
      const product: IProduct = { id: 24261 };
      inventoryTransaction.product = product;

      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inventoryTransaction));
      expect(comp.productsCollection).toContain(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InventoryTransaction>>();
      const inventoryTransaction = { id: 123 };
      jest.spyOn(inventoryTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryTransaction }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventoryTransactionService.update).toHaveBeenCalledWith(inventoryTransaction);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InventoryTransaction>>();
      const inventoryTransaction = new InventoryTransaction();
      jest.spyOn(inventoryTransactionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inventoryTransaction }));
      saveSubject.complete();

      // THEN
      expect(inventoryTransactionService.create).toHaveBeenCalledWith(inventoryTransaction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InventoryTransaction>>();
      const inventoryTransaction = { id: 123 };
      jest.spyOn(inventoryTransactionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventoryTransaction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventoryTransactionService.update).toHaveBeenCalledWith(inventoryTransaction);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProductById', () => {
      it('Should return tracked Product primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProductById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
