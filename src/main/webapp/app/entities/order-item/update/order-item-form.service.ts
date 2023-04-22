import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrderItem, NewOrderItem } from '../order-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrderItem for edit and NewOrderItemFormGroupInput for create.
 */
type OrderItemFormGroupInput = IOrderItem | PartialWithRequiredKeyOf<NewOrderItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrderItem | NewOrderItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type OrderItemFormRawValue = FormValueOf<IOrderItem>;

type NewOrderItemFormRawValue = FormValueOf<NewOrderItem>;

type OrderItemFormDefaults = Pick<NewOrderItem, 'id' | 'createdDate' | 'lastModifiedDate'>;

type OrderItemFormGroupContent = {
  id: FormControl<OrderItemFormRawValue['id'] | NewOrderItem['id']>;
  qty: FormControl<OrderItemFormRawValue['qty']>;
  totalPrice: FormControl<OrderItemFormRawValue['totalPrice']>;
  createdBy: FormControl<OrderItemFormRawValue['createdBy']>;
  createdDate: FormControl<OrderItemFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<OrderItemFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<OrderItemFormRawValue['lastModifiedDate']>;
  order: FormControl<OrderItemFormRawValue['order']>;
};

export type OrderItemFormGroup = FormGroup<OrderItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderItemFormService {
  createOrderItemFormGroup(orderItem: OrderItemFormGroupInput = { id: null }): OrderItemFormGroup {
    const orderItemRawValue = this.convertOrderItemToOrderItemRawValue({
      ...this.getFormDefaults(),
      ...orderItem,
    });
    return new FormGroup<OrderItemFormGroupContent>({
      id: new FormControl(
        { value: orderItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      qty: new FormControl(orderItemRawValue.qty, {
        validators: [Validators.required],
      }),
      totalPrice: new FormControl(orderItemRawValue.totalPrice, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(orderItemRawValue.createdBy),
      createdDate: new FormControl(orderItemRawValue.createdDate),
      lastModifiedBy: new FormControl(orderItemRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(orderItemRawValue.lastModifiedDate),
      order: new FormControl(orderItemRawValue.order),
    });
  }

  getOrderItem(form: OrderItemFormGroup): IOrderItem | NewOrderItem {
    return this.convertOrderItemRawValueToOrderItem(form.getRawValue() as OrderItemFormRawValue | NewOrderItemFormRawValue);
  }

  resetForm(form: OrderItemFormGroup, orderItem: OrderItemFormGroupInput): void {
    const orderItemRawValue = this.convertOrderItemToOrderItemRawValue({ ...this.getFormDefaults(), ...orderItem });
    form.reset(
      {
        ...orderItemRawValue,
        id: { value: orderItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrderItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertOrderItemRawValueToOrderItem(rawOrderItem: OrderItemFormRawValue | NewOrderItemFormRawValue): IOrderItem | NewOrderItem {
    return {
      ...rawOrderItem,
      createdDate: dayjs(rawOrderItem.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawOrderItem.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrderItemToOrderItemRawValue(
    orderItem: IOrderItem | (Partial<NewOrderItem> & OrderItemFormDefaults)
  ): OrderItemFormRawValue | PartialWithRequiredKeyOf<NewOrderItemFormRawValue> {
    return {
      ...orderItem,
      createdDate: orderItem.createdDate ? orderItem.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: orderItem.lastModifiedDate ? orderItem.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
