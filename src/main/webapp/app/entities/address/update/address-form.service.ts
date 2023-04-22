import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAddress | NewAddress> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type AddressFormRawValue = FormValueOf<IAddress>;

type NewAddressFormRawValue = FormValueOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id' | 'createdDate' | 'lastModifiedDate'>;

type AddressFormGroupContent = {
  id: FormControl<AddressFormRawValue['id'] | NewAddress['id']>;
  street1: FormControl<AddressFormRawValue['street1']>;
  street2: FormControl<AddressFormRawValue['street2']>;
  city: FormControl<AddressFormRawValue['city']>;
  postalCode: FormControl<AddressFormRawValue['postalCode']>;
  createdBy: FormControl<AddressFormRawValue['createdBy']>;
  createdDate: FormControl<AddressFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<AddressFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<AddressFormRawValue['lastModifiedDate']>;
  orderSA: FormControl<AddressFormRawValue['orderSA']>;
  orderBA: FormControl<AddressFormRawValue['orderBA']>;
  store: FormControl<AddressFormRawValue['store']>;
  restaurant: FormControl<AddressFormRawValue['restaurant']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = this.convertAddressToAddressRawValue({
      ...this.getFormDefaults(),
      ...address,
    });
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      street1: new FormControl(addressRawValue.street1, {
        validators: [Validators.required],
      }),
      street2: new FormControl(addressRawValue.street2),
      city: new FormControl(addressRawValue.city),
      postalCode: new FormControl(addressRawValue.postalCode, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(addressRawValue.createdBy),
      createdDate: new FormControl(addressRawValue.createdDate),
      lastModifiedBy: new FormControl(addressRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(addressRawValue.lastModifiedDate),
      orderSA: new FormControl(addressRawValue.orderSA),
      orderBA: new FormControl(addressRawValue.orderBA),
      store: new FormControl(addressRawValue.store),
      restaurant: new FormControl(addressRawValue.restaurant),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return this.convertAddressRawValueToAddress(form.getRawValue() as AddressFormRawValue | NewAddressFormRawValue);
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = this.convertAddressToAddressRawValue({ ...this.getFormDefaults(), ...address });
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertAddressRawValueToAddress(rawAddress: AddressFormRawValue | NewAddressFormRawValue): IAddress | NewAddress {
    return {
      ...rawAddress,
      createdDate: dayjs(rawAddress.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawAddress.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertAddressToAddressRawValue(
    address: IAddress | (Partial<NewAddress> & AddressFormDefaults)
  ): AddressFormRawValue | PartialWithRequiredKeyOf<NewAddressFormRawValue> {
    return {
      ...address,
      createdDate: address.createdDate ? address.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: address.lastModifiedDate ? address.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
