import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductInfo, NewProductInfo } from '../product-info.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductInfo for edit and NewProductInfoFormGroupInput for create.
 */
type ProductInfoFormGroupInput = IProductInfo | PartialWithRequiredKeyOf<NewProductInfo>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductInfo | NewProductInfo> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ProductInfoFormRawValue = FormValueOf<IProductInfo>;

type NewProductInfoFormRawValue = FormValueOf<NewProductInfo>;

type ProductInfoFormDefaults = Pick<NewProductInfo, 'id' | 'isGlutenFree' | 'createdDate' | 'lastModifiedDate'>;

type ProductInfoFormGroupContent = {
  id: FormControl<ProductInfoFormRawValue['id'] | NewProductInfo['id']>;
  qtyInStock: FormControl<ProductInfoFormRawValue['qtyInStock']>;
  isGlutenFree: FormControl<ProductInfoFormRawValue['isGlutenFree']>;
  createdBy: FormControl<ProductInfoFormRawValue['createdBy']>;
  createdDate: FormControl<ProductInfoFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ProductInfoFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ProductInfoFormRawValue['lastModifiedDate']>;
  product: FormControl<ProductInfoFormRawValue['product']>;
};

export type ProductInfoFormGroup = FormGroup<ProductInfoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductInfoFormService {
  createProductInfoFormGroup(productInfo: ProductInfoFormGroupInput = { id: null }): ProductInfoFormGroup {
    const productInfoRawValue = this.convertProductInfoToProductInfoRawValue({
      ...this.getFormDefaults(),
      ...productInfo,
    });
    return new FormGroup<ProductInfoFormGroupContent>({
      id: new FormControl(
        { value: productInfoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      qtyInStock: new FormControl(productInfoRawValue.qtyInStock, {
        validators: [Validators.required],
      }),
      isGlutenFree: new FormControl(productInfoRawValue.isGlutenFree, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(productInfoRawValue.createdBy),
      createdDate: new FormControl(productInfoRawValue.createdDate),
      lastModifiedBy: new FormControl(productInfoRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(productInfoRawValue.lastModifiedDate),
      product: new FormControl(productInfoRawValue.product),
    });
  }

  getProductInfo(form: ProductInfoFormGroup): IProductInfo | NewProductInfo {
    return this.convertProductInfoRawValueToProductInfo(form.getRawValue() as ProductInfoFormRawValue | NewProductInfoFormRawValue);
  }

  resetForm(form: ProductInfoFormGroup, productInfo: ProductInfoFormGroupInput): void {
    const productInfoRawValue = this.convertProductInfoToProductInfoRawValue({ ...this.getFormDefaults(), ...productInfo });
    form.reset(
      {
        ...productInfoRawValue,
        id: { value: productInfoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductInfoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isGlutenFree: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertProductInfoRawValueToProductInfo(
    rawProductInfo: ProductInfoFormRawValue | NewProductInfoFormRawValue
  ): IProductInfo | NewProductInfo {
    return {
      ...rawProductInfo,
      createdDate: dayjs(rawProductInfo.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawProductInfo.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductInfoToProductInfoRawValue(
    productInfo: IProductInfo | (Partial<NewProductInfo> & ProductInfoFormDefaults)
  ): ProductInfoFormRawValue | PartialWithRequiredKeyOf<NewProductInfoFormRawValue> {
    return {
      ...productInfo,
      createdDate: productInfo.createdDate ? productInfo.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: productInfo.lastModifiedDate ? productInfo.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
