import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-info.test-samples';

import { ProductInfoFormService } from './product-info-form.service';

describe('ProductInfo Form Service', () => {
  let service: ProductInfoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductInfoFormService);
  });

  describe('Service methods', () => {
    describe('createProductInfoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductInfoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            qtyInStock: expect.any(Object),
            isGlutenFree: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });

      it('passing IProductInfo should create a new form with FormGroup', () => {
        const formGroup = service.createProductInfoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            qtyInStock: expect.any(Object),
            isGlutenFree: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            product: expect.any(Object),
          })
        );
      });
    });

    describe('getProductInfo', () => {
      it('should return NewProductInfo for default ProductInfo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductInfoFormGroup(sampleWithNewData);

        const productInfo = service.getProductInfo(formGroup) as any;

        expect(productInfo).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductInfo for empty ProductInfo initial value', () => {
        const formGroup = service.createProductInfoFormGroup();

        const productInfo = service.getProductInfo(formGroup) as any;

        expect(productInfo).toMatchObject({});
      });

      it('should return IProductInfo', () => {
        const formGroup = service.createProductInfoFormGroup(sampleWithRequiredData);

        const productInfo = service.getProductInfo(formGroup) as any;

        expect(productInfo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductInfo should not enable id FormControl', () => {
        const formGroup = service.createProductInfoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductInfo should disable id FormControl', () => {
        const formGroup = service.createProductInfoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
