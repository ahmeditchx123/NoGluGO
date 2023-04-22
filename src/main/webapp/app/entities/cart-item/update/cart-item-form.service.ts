import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICartItem, NewCartItem } from '../cart-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICartItem for edit and NewCartItemFormGroupInput for create.
 */
type CartItemFormGroupInput = ICartItem | PartialWithRequiredKeyOf<NewCartItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICartItem | NewCartItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type CartItemFormRawValue = FormValueOf<ICartItem>;

type NewCartItemFormRawValue = FormValueOf<NewCartItem>;

type CartItemFormDefaults = Pick<NewCartItem, 'id' | 'createdDate' | 'lastModifiedDate'>;

type CartItemFormGroupContent = {
  id: FormControl<CartItemFormRawValue['id'] | NewCartItem['id']>;
  qty: FormControl<CartItemFormRawValue['qty']>;
  totalPrice: FormControl<CartItemFormRawValue['totalPrice']>;
  createdBy: FormControl<CartItemFormRawValue['createdBy']>;
  createdDate: FormControl<CartItemFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<CartItemFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<CartItemFormRawValue['lastModifiedDate']>;
  cart: FormControl<CartItemFormRawValue['cart']>;
};

export type CartItemFormGroup = FormGroup<CartItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CartItemFormService {
  createCartItemFormGroup(cartItem: CartItemFormGroupInput = { id: null }): CartItemFormGroup {
    const cartItemRawValue = this.convertCartItemToCartItemRawValue({
      ...this.getFormDefaults(),
      ...cartItem,
    });
    return new FormGroup<CartItemFormGroupContent>({
      id: new FormControl(
        { value: cartItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      qty: new FormControl(cartItemRawValue.qty, {
        validators: [Validators.required],
      }),
      totalPrice: new FormControl(cartItemRawValue.totalPrice, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(cartItemRawValue.createdBy),
      createdDate: new FormControl(cartItemRawValue.createdDate),
      lastModifiedBy: new FormControl(cartItemRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(cartItemRawValue.lastModifiedDate),
      cart: new FormControl(cartItemRawValue.cart),
    });
  }

  getCartItem(form: CartItemFormGroup): ICartItem | NewCartItem {
    return this.convertCartItemRawValueToCartItem(form.getRawValue() as CartItemFormRawValue | NewCartItemFormRawValue);
  }

  resetForm(form: CartItemFormGroup, cartItem: CartItemFormGroupInput): void {
    const cartItemRawValue = this.convertCartItemToCartItemRawValue({ ...this.getFormDefaults(), ...cartItem });
    form.reset(
      {
        ...cartItemRawValue,
        id: { value: cartItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CartItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertCartItemRawValueToCartItem(rawCartItem: CartItemFormRawValue | NewCartItemFormRawValue): ICartItem | NewCartItem {
    return {
      ...rawCartItem,
      createdDate: dayjs(rawCartItem.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawCartItem.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertCartItemToCartItemRawValue(
    cartItem: ICartItem | (Partial<NewCartItem> & CartItemFormDefaults)
  ): CartItemFormRawValue | PartialWithRequiredKeyOf<NewCartItemFormRawValue> {
    return {
      ...cartItem,
      createdDate: cartItem.createdDate ? cartItem.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: cartItem.lastModifiedDate ? cartItem.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
