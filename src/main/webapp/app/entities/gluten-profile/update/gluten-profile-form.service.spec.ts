import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../gluten-profile.test-samples';

import { GlutenProfileFormService } from './gluten-profile-form.service';

describe('GlutenProfile Form Service', () => {
  let service: GlutenProfileFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlutenProfileFormService);
  });

  describe('Service methods', () => {
    describe('createGlutenProfileFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGlutenProfileFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            diseas: expect.any(Object),
            otherDiseas: expect.any(Object),
            strictnessLevel: expect.any(Object),
            diaryFreePreferenceLvl: expect.any(Object),
            veganPreferenceLvl: expect.any(Object),
            ketoPreferenceLvl: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            userId: expect.any(Object),
          })
        );
      });

      it('passing IGlutenProfile should create a new form with FormGroup', () => {
        const formGroup = service.createGlutenProfileFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            diseas: expect.any(Object),
            otherDiseas: expect.any(Object),
            strictnessLevel: expect.any(Object),
            diaryFreePreferenceLvl: expect.any(Object),
            veganPreferenceLvl: expect.any(Object),
            ketoPreferenceLvl: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            userId: expect.any(Object),
          })
        );
      });
    });

    describe('getGlutenProfile', () => {
      it('should return NewGlutenProfile for default GlutenProfile initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGlutenProfileFormGroup(sampleWithNewData);

        const glutenProfile = service.getGlutenProfile(formGroup) as any;

        expect(glutenProfile).toMatchObject(sampleWithNewData);
      });

      it('should return NewGlutenProfile for empty GlutenProfile initial value', () => {
        const formGroup = service.createGlutenProfileFormGroup();

        const glutenProfile = service.getGlutenProfile(formGroup) as any;

        expect(glutenProfile).toMatchObject({});
      });

      it('should return IGlutenProfile', () => {
        const formGroup = service.createGlutenProfileFormGroup(sampleWithRequiredData);

        const glutenProfile = service.getGlutenProfile(formGroup) as any;

        expect(glutenProfile).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGlutenProfile should not enable id FormControl', () => {
        const formGroup = service.createGlutenProfileFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGlutenProfile should disable id FormControl', () => {
        const formGroup = service.createGlutenProfileFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
