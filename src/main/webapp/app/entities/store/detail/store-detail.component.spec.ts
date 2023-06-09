import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StoreDetailComponent } from './store-detail.component';

describe('Store Management Detail Component', () => {
  let comp: StoreDetailComponent;
  let fixture: ComponentFixture<StoreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ store: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(StoreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StoreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load store on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.store).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
