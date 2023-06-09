import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProduct, NewProduct } from '../product.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProduct for edit and NewProductFormGroupInput for create.
 */
type ProductFormGroupInput = IProduct | PartialWithRequiredKeyOf<NewProduct>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProduct | NewProduct> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type ProductFormRawValue = FormValueOf<IProduct>;

type NewProductFormRawValue = FormValueOf<NewProduct>;

type ProductFormDefaults = Pick<NewProduct, 'id' | 'isAvailable' | 'createdDate' | 'lastModifiedDate'>;

type ProductFormGroupContent = {
  id: FormControl<ProductFormRawValue['id'] | NewProduct['id']>;
  name: FormControl<ProductFormRawValue['name']>;
  sku: FormControl<ProductFormRawValue['sku']>;
  description: FormControl<ProductFormRawValue['description']>;
  imgPath: FormControl<ProductFormRawValue['imgPath']>;
  unitPrice: FormControl<ProductFormRawValue['unitPrice']>;
  isAvailable: FormControl<ProductFormRawValue['isAvailable']>;
  createdBy: FormControl<ProductFormRawValue['createdBy']>;
  createdDate: FormControl<ProductFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<ProductFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<ProductFormRawValue['lastModifiedDate']>;
  cartItem: FormControl<ProductFormRawValue['cartItem']>;
  orderItem: FormControl<ProductFormRawValue['orderItem']>;
  store: FormControl<ProductFormRawValue['store']>;
};

export type ProductFormGroup = FormGroup<ProductFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductFormService {
  createProductFormGroup(product: ProductFormGroupInput = { id: null }): ProductFormGroup {
    const productRawValue = this.convertProductToProductRawValue({
      ...this.getFormDefaults(),
      ...product,
    });
    return new FormGroup<ProductFormGroupContent>({
      id: new FormControl(
        { value: productRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(productRawValue.name, {
        validators: [Validators.required],
      }),
      sku: new FormControl(productRawValue.sku, {
        validators: [Validators.required],
      }),
      description: new FormControl(productRawValue.description),
      imgPath: new FormControl(productRawValue.imgPath, {
        validators: [Validators.required],
      }),
      unitPrice: new FormControl(productRawValue.unitPrice, {
        validators: [Validators.required],
      }),
      isAvailable: new FormControl(productRawValue.isAvailable, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(productRawValue.createdBy),
      createdDate: new FormControl(productRawValue.createdDate),
      lastModifiedBy: new FormControl(productRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(productRawValue.lastModifiedDate),
      cartItem: new FormControl(productRawValue.cartItem),
      orderItem: new FormControl(productRawValue.orderItem),
      store: new FormControl(productRawValue.store),
    });
  }

  getProduct(form: ProductFormGroup): IProduct | NewProduct {
    return this.convertProductRawValueToProduct(form.getRawValue() as ProductFormRawValue | NewProductFormRawValue);
  }

  resetForm(form: ProductFormGroup, product: ProductFormGroupInput): void {
    const productRawValue = this.convertProductToProductRawValue({ ...this.getFormDefaults(), ...product });
    form.reset(
      {
        ...productRawValue,
        id: { value: productRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isAvailable: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertProductRawValueToProduct(rawProduct: ProductFormRawValue | NewProductFormRawValue): IProduct | NewProduct {
    return {
      ...rawProduct,
      createdDate: dayjs(rawProduct.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawProduct.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertProductToProductRawValue(
    product: IProduct | (Partial<NewProduct> & ProductFormDefaults)
  ): ProductFormRawValue | PartialWithRequiredKeyOf<NewProductFormRawValue> {
    return {
      ...product,
      createdDate: product.createdDate ? product.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: product.lastModifiedDate ? product.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
