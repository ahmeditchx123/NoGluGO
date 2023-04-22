import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../menu-item.test-samples';

import { MenuItemFormService } from './menu-item-form.service';

describe('MenuItem Form Service', () => {
  let service: MenuItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MenuItemFormService);
  });

  describe('Service methods', () => {
    describe('createMenuItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMenuItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            content: expect.any(Object),
            imgPath: expect.any(Object),
            unitPrice: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            menu: expect.any(Object),
          })
        );
      });

      it('passing IMenuItem should create a new form with FormGroup', () => {
        const formGroup = service.createMenuItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            content: expect.any(Object),
            imgPath: expect.any(Object),
            unitPrice: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            menu: expect.any(Object),
          })
        );
      });
    });

    describe('getMenuItem', () => {
      it('should return NewMenuItem for default MenuItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMenuItemFormGroup(sampleWithNewData);

        const menuItem = service.getMenuItem(formGroup) as any;

        expect(menuItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewMenuItem for empty MenuItem initial value', () => {
        const formGroup = service.createMenuItemFormGroup();

        const menuItem = service.getMenuItem(formGroup) as any;

        expect(menuItem).toMatchObject({});
      });

      it('should return IMenuItem', () => {
        const formGroup = service.createMenuItemFormGroup(sampleWithRequiredData);

        const menuItem = service.getMenuItem(formGroup) as any;

        expect(menuItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMenuItem should not enable id FormControl', () => {
        const formGroup = service.createMenuItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMenuItem should disable id FormControl', () => {
        const formGroup = service.createMenuItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
