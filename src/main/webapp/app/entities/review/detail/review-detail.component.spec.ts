import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReviewDetailComponent } from './review-detail.component';

describe('Review Management Detail Component', () => {
  let comp: ReviewDetailComponent;
  let fixture: ComponentFixture<ReviewDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReviewDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ review: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(ReviewDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load review on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.review).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
