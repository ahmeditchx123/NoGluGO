import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICart, NewCart } from '../cart.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICart for edit and NewCartFormGroupInput for create.
 */
type CartFormGroupInput = ICart | PartialWithRequiredKeyOf<NewCart>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICart | NewCart> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type CartFormRawValue = FormValueOf<ICart>;

type NewCartFormRawValue = FormValueOf<NewCart>;

type CartFormDefaults = Pick<NewCart, 'id' | 'createdDate' | 'lastModifiedDate'>;

type CartFormGroupContent = {
  id: FormControl<CartFormRawValue['id'] | NewCart['id']>;
  totalItems: FormControl<CartFormRawValue['totalItems']>;
  totalPrice: FormControl<CartFormRawValue['totalPrice']>;
  createdBy: FormControl<CartFormRawValue['createdBy']>;
  createdDate: FormControl<CartFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<CartFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<CartFormRawValue['lastModifiedDate']>;
  userId: FormControl<CartFormRawValue['userId']>;
};

export type CartFormGroup = FormGroup<CartFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CartFormService {
  createCartFormGroup(cart: CartFormGroupInput = { id: null }): CartFormGroup {
    const cartRawValue = this.convertCartToCartRawValue({
      ...this.getFormDefaults(),
      ...cart,
    });
    return new FormGroup<CartFormGroupContent>({
      id: new FormControl(
        { value: cartRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      totalItems: new FormControl(cartRawValue.totalItems),
      totalPrice: new FormControl(cartRawValue.totalPrice),
      createdBy: new FormControl(cartRawValue.createdBy),
      createdDate: new FormControl(cartRawValue.createdDate),
      lastModifiedBy: new FormControl(cartRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(cartRawValue.lastModifiedDate),
      userId: new FormControl(cartRawValue.userId),
    });
  }

  getCart(form: CartFormGroup): ICart | NewCart {
    return this.convertCartRawValueToCart(form.getRawValue() as CartFormRawValue | NewCartFormRawValue);
  }

  resetForm(form: CartFormGroup, cart: CartFormGroupInput): void {
    const cartRawValue = this.convertCartToCartRawValue({ ...this.getFormDefaults(), ...cart });
    form.reset(
      {
        ...cartRawValue,
        id: { value: cartRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CartFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertCartRawValueToCart(rawCart: CartFormRawValue | NewCartFormRawValue): ICart | NewCart {
    return {
      ...rawCart,
      createdDate: dayjs(rawCart.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawCart.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCartToCartRawValue(
    cart: ICart | (Partial<NewCart> & CartFormDefaults)
  ): CartFormRawValue | PartialWithRequiredKeyOf<NewCartFormRawValue> {
    return {
      ...cart,
      createdDate: cart.createdDate ? cart.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: cart.lastModifiedDate ? cart.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
