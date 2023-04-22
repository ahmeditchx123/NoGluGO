import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderItemDetailComponent } from './order-item-detail.component';

describe('OrderItem Management Detail Component', () => {
  let comp: OrderItemDetailComponent;
  let fixture: ComponentFixture<OrderItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ orderItem: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(OrderItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load orderItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.orderItem).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
