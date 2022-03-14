import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PurchaseOrderItemService } from '../service/purchase-order-item.service';

import { PurchaseOrderItemComponent } from './purchase-order-item.component';

describe('PurchaseOrderItem Management Component', () => {
  let comp: PurchaseOrderItemComponent;
  let fixture: ComponentFixture<PurchaseOrderItemComponent>;
  let service: PurchaseOrderItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PurchaseOrderItemComponent],
    })
      .overrideTemplate(PurchaseOrderItemComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderItemComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PurchaseOrderItemService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.purchaseOrderItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
