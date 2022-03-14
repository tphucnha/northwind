import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InventoryTransactionDetailComponent } from './inventory-transaction-detail.component';

describe('InventoryTransaction Management Detail Component', () => {
  let comp: InventoryTransactionDetailComponent;
  let fixture: ComponentFixture<InventoryTransactionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InventoryTransactionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inventoryTransaction: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InventoryTransactionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InventoryTransactionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inventoryTransaction on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inventoryTransaction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
