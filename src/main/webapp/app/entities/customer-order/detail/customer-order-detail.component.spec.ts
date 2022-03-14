import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CustomerOrderDetailComponent } from './customer-order-detail.component';

describe('CustomerOrder Management Detail Component', () => {
  let comp: CustomerOrderDetailComponent;
  let fixture: ComponentFixture<CustomerOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ customerOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CustomerOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CustomerOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load customerOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.customerOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
