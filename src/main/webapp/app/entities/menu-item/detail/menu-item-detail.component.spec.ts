import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MenuItemDetailComponent } from './menu-item-detail.component';

describe('MenuItem Management Detail Component', () => {
  let comp: MenuItemDetailComponent;
  let fixture: ComponentFixture<MenuItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MenuItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ menuItem: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(MenuItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(MenuItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load menuItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.menuItem).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
