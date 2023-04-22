import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductInfoDetailComponent } from './product-info-detail.component';

describe('ProductInfo Management Detail Component', () => {
  let comp: ProductInfoDetailComponent;
  let fixture: ComponentFixture<ProductInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productInfo: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(ProductInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productInfo).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
