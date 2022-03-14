import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { SupplierService } from 'app/entities/supplier/service/supplier.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategory[] = [];
  suppliersSharedCollection: ISupplier[] = [];

  editForm = this.fb.group({
    id: [],
    code: [],
    name: [],
    description: [],
    standardCost: [],
    listPrice: [],
    reorderLevel: [],
    targetLevel: [],
    quantityPerUnit: [],
    disContinued: [],
    minimumReorderQuantity: [],
    category: [],
    suppliers: [],
  });

  constructor(
    protected productService: ProductService,
    protected categoryService: CategoryService,
    protected supplierService: SupplierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackCategoryById(index: number, item: ICategory): number {
    return item.id!;
  }

  trackSupplierById(index: number, item: ISupplier): number {
    return item.id!;
  }

  getSelectedSupplier(option: ISupplier, selectedVals?: ISupplier[]): ISupplier {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      code: product.code,
      name: product.name,
      description: product.description,
      standardCost: product.standardCost,
      listPrice: product.listPrice,
      reorderLevel: product.reorderLevel,
      targetLevel: product.targetLevel,
      quantityPerUnit: product.quantityPerUnit,
      disContinued: product.disContinued,
      minimumReorderQuantity: product.minimumReorderQuantity,
      category: product.category,
      suppliers: product.suppliers,
    });

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing(
      this.categoriesSharedCollection,
      product.category
    );
    this.suppliersSharedCollection = this.supplierService.addSupplierToCollectionIfMissing(
      this.suppliersSharedCollection,
      ...(product.suppliers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing(categories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.supplierService
      .query()
      .pipe(map((res: HttpResponse<ISupplier[]>) => res.body ?? []))
      .pipe(
        map((suppliers: ISupplier[]) =>
          this.supplierService.addSupplierToCollectionIfMissing(suppliers, ...(this.editForm.get('suppliers')!.value ?? []))
        )
      )
      .subscribe((suppliers: ISupplier[]) => (this.suppliersSharedCollection = suppliers));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      standardCost: this.editForm.get(['standardCost'])!.value,
      listPrice: this.editForm.get(['listPrice'])!.value,
      reorderLevel: this.editForm.get(['reorderLevel'])!.value,
      targetLevel: this.editForm.get(['targetLevel'])!.value,
      quantityPerUnit: this.editForm.get(['quantityPerUnit'])!.value,
      disContinued: this.editForm.get(['disContinued'])!.value,
      minimumReorderQuantity: this.editForm.get(['minimumReorderQuantity'])!.value,
      category: this.editForm.get(['category'])!.value,
      suppliers: this.editForm.get(['suppliers'])!.value,
    };
  }
}
