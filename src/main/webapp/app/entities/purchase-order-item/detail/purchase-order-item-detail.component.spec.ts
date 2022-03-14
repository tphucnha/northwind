import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PurchaseOrderItemDetailComponent } from './purchase-order-item-detail.component';

describe('PurchaseOrderItem Management Detail Component', () => {
  let comp: PurchaseOrderItemDetailComponent;
  let fixture: ComponentFixture<PurchaseOrderItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PurchaseOrderItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ purchaseOrderItem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PurchaseOrderItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PurchaseOrderItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load purchaseOrderItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.purchaseOrderItem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
