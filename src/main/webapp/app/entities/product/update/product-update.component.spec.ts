import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductService } from '../service/product.service';
import { IProduct, Product } from '../product.model';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productService: ProductService;
  let categoryService: CategoryService;
  let supplierService: SupplierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductUpdateComponent],
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
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productService = TestBed.inject(ProductService);
    categoryService = TestBed.inject(CategoryService);
    supplierService = TestBed.inject(SupplierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const category: ICategory = { id: 52954 };
      product.category = category;

      const categoryCollection: ICategory[] = [{ id: 63621 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(categoryCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Supplier query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const suppliers: ISupplier[] = [{ id: 81098 }];
      product.suppliers = suppliers;

      const supplierCollection: ISupplier[] = [{ id: 87445 }];
      jest.spyOn(supplierService, 'query').mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [...suppliers];
      const expectedCollection: ISupplier[] = [...additionalSuppliers, ...supplierCollection];
      jest.spyOn(supplierService, 'addSupplierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(supplierService.addSupplierToCollectionIfMissing).toHaveBeenCalledWith(supplierCollection, ...additionalSuppliers);
      expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const category: ICategory = { id: 32126 };
      product.category = category;
      const suppliers: ISupplier = { id: 38413 };
      product.suppliers = [suppliers];

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(product));
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.suppliersSharedCollection).toContain(suppliers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = new Product();
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productService.create).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Product>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalledWith(product);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategoryById', () => {
      it('Should return tracked Category primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategoryById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSupplierById', () => {
      it('Should return tracked Supplier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSupplierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedSupplier', () => {
      it('Should return option if no Supplier is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedSupplier(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Supplier for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedSupplier(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Supplier is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedSupplier(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
