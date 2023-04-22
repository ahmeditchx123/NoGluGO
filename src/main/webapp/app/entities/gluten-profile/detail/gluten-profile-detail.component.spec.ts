import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GlutenProfileDetailComponent } from './gluten-profile-detail.component';

describe('GlutenProfile Management Detail Component', () => {
  let comp: GlutenProfileDetailComponent;
  let fixture: ComponentFixture<GlutenProfileDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GlutenProfileDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ glutenProfile: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(GlutenProfileDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GlutenProfileDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load glutenProfile on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.glutenProfile).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
