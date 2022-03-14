import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PurchaseOrderItemService } from '../service/purchase-order-item.service';
import { IPurchaseOrderItem, PurchaseOrderItem } from '../purchase-order-item.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';

import { PurchaseOrderItemUpdateComponent } from './purchase-order-item-update.component';

describe('PurchaseOrderItem Management Update Component', () => {
  let comp: PurchaseOrderItemUpdateComponent;
  let fixture: ComponentFixture<PurchaseOrderItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseOrderItemService: PurchaseOrderItemService;
  let productService: ProductService;
  let purchaseOrderService: PurchaseOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PurchaseOrderItemUpdateComponent],
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
      .overrideTemplate(PurchaseOrderItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseOrderItemService = TestBed.inject(PurchaseOrderItemService);
    productService = TestBed.inject(ProductService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call product query and add missing value', () => {
      const purchaseOrderItem: IPurchaseOrderItem = { id: 456 };
      const product: IProduct = { id: 92940 };
      purchaseOrderItem.product = product;

      const productCollection: IProduct[] = [{ id: 42325 }];
      jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
      const expectedCollection: IProduct[] = [product, ...productCollection];
      jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      expect(productService.query).toHaveBeenCalled();
      expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, product);
      expect(comp.productsCollection).toEqual(expectedCollection);
    });

    it('Should call PurchaseOrder query and add missing value', () => {
      const purchaseOrderItem: IPurchaseOrderItem = { id: 456 };
      const purchaseOrder: IPurchaseOrder = { id: 74629 };
      purchaseOrderItem.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 97004 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const purchaseOrderItem: IPurchaseOrderItem = { id: 456 };
      const product: IProduct = { id: 94965 };
      purchaseOrderItem.product = product;
      const purchaseOrder: IPurchaseOrder = { id: 2125 };
      purchaseOrderItem.purchaseOrder = purchaseOrder;

      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(purchaseOrderItem));
      expect(comp.productsCollection).toContain(product);
      expect(comp.purchaseOrdersSharedCollection).toContain(purchaseOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrderItem>>();
      const purchaseOrderItem = { id: 123 };
      jest.spyOn(purchaseOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrderItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseOrderItemService.update).toHaveBeenCalledWith(purchaseOrderItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrderItem>>();
      const purchaseOrderItem = new PurchaseOrderItem();
      jest.spyOn(purchaseOrderItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrderItem }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderItemService.create).toHaveBeenCalledWith(purchaseOrderItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<PurchaseOrderItem>>();
      const purchaseOrderItem = { id: 123 };
      jest.spyOn(purchaseOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchaseOrderItemService.update).toHaveBeenCalledWith(purchaseOrderItem);
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

    describe('trackPurchaseOrderById', () => {
      it('Should return tracked PurchaseOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPurchaseOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
