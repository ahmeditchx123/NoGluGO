import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MenuItemFormService } from './menu-item-form.service';
import { MenuItemService } from '../service/menu-item.service';
import { IMenuItem } from '../menu-item.model';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

import { MenuItemUpdateComponent } from './menu-item-update.component';

describe('MenuItem Management Update Component', () => {
  let comp: MenuItemUpdateComponent;
  let fixture: ComponentFixture<MenuItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let menuItemFormService: MenuItemFormService;
  let menuItemService: MenuItemService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MenuItemUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MenuItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MenuItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    menuItemFormService = TestBed.inject(MenuItemFormService);
    menuItemService = TestBed.inject(MenuItemService);
    menuService = TestBed.inject(MenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Menu query and add missing value', () => {
      const menuItem: IMenuItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const menu: IMenu = { id: '305113a8-8705-4f6a-bab9-c5adf9cc730f' };
      menuItem.menu = menu;

      const menuCollection: IMenu[] = [{ id: '71f488fe-70a0-42b2-b5b2-b089560c367a' }];
      jest.spyOn(menuService, 'query').mockReturnValue(of(new HttpResponse({ body: menuCollection })));
      const additionalMenus = [menu];
      const expectedCollection: IMenu[] = [...additionalMenus, ...menuCollection];
      jest.spyOn(menuService, 'addMenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(menuService.query).toHaveBeenCalled();
      expect(menuService.addMenuToCollectionIfMissing).toHaveBeenCalledWith(
        menuCollection,
        ...additionalMenus.map(expect.objectContaining)
      );
      expect(comp.menusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const menuItem: IMenuItem = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
      const menu: IMenu = { id: 'f992de3b-805d-4239-b12d-01e12e0e87e1' };
      menuItem.menu = menu;

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(comp.menusSharedCollection).toContain(menu);
      expect(comp.menuItem).toEqual(menuItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMenuItem>>();
      const menuItem = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(menuItemFormService, 'getMenuItem').mockReturnValue(menuItem);
      jest.spyOn(menuItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menuItem }));
      saveSubject.complete();

      // THEN
      expect(menuItemFormService.getMenuItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(menuItemService.update).toHaveBeenCalledWith(expect.objectContaining(menuItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMenuItem>>();
      const menuItem = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(menuItemFormService, 'getMenuItem').mockReturnValue({ id: null });
      jest.spyOn(menuItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menuItem }));
      saveSubject.complete();

      // THEN
      expect(menuItemFormService.getMenuItem).toHaveBeenCalled();
      expect(menuItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMenuItem>>();
      const menuItem = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(menuItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(menuItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMenu', () => {
      it('Should forward to menuService', () => {
        const entity = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const entity2 = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        jest.spyOn(menuService, 'compareMenu');
        comp.compareMenu(entity, entity2);
        expect(menuService.compareMenu).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
