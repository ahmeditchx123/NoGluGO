import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderDetailComponent } from './order-detail.component';

describe('Order Management Detail Component', () => {
  let comp: OrderDetailComponent;
  let fixture: ComponentFixture<OrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ order: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(OrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load order on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.order).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
