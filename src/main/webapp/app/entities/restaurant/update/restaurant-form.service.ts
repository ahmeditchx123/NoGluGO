import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRestaurant, NewRestaurant } from '../restaurant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRestaurant for edit and NewRestaurantFormGroupInput for create.
 */
type RestaurantFormGroupInput = IRestaurant | PartialWithRequiredKeyOf<NewRestaurant>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRestaurant | NewRestaurant> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type RestaurantFormRawValue = FormValueOf<IRestaurant>;

type NewRestaurantFormRawValue = FormValueOf<NewRestaurant>;

type RestaurantFormDefaults = Pick<NewRestaurant, 'id' | 'isDedicatedGlutenFree' | 'createdDate' | 'lastModifiedDate'>;

type RestaurantFormGroupContent = {
  id: FormControl<RestaurantFormRawValue['id'] | NewRestaurant['id']>;
  name: FormControl<RestaurantFormRawValue['name']>;
  description: FormControl<RestaurantFormRawValue['description']>;
  telephone: FormControl<RestaurantFormRawValue['telephone']>;
  imgPath: FormControl<RestaurantFormRawValue['imgPath']>;
  isDedicatedGlutenFree: FormControl<RestaurantFormRawValue['isDedicatedGlutenFree']>;
  website: FormControl<RestaurantFormRawValue['website']>;
  tableNumber: FormControl<RestaurantFormRawValue['tableNumber']>;
  createdBy: FormControl<RestaurantFormRawValue['createdBy']>;
  createdDate: FormControl<RestaurantFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<RestaurantFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<RestaurantFormRawValue['lastModifiedDate']>;
};

export type RestaurantFormGroup = FormGroup<RestaurantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RestaurantFormService {
  createRestaurantFormGroup(restaurant: RestaurantFormGroupInput = { id: null }): RestaurantFormGroup {
    const restaurantRawValue = this.convertRestaurantToRestaurantRawValue({
      ...this.getFormDefaults(),
      ...restaurant,
    });
    return new FormGroup<RestaurantFormGroupContent>({
      id: new FormControl(
        { value: restaurantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(restaurantRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(restaurantRawValue.description),
      telephone: new FormControl(restaurantRawValue.telephone, {
        validators: [Validators.required],
      }),
      imgPath: new FormControl(restaurantRawValue.imgPath, {
        validators: [Validators.required],
      }),
      isDedicatedGlutenFree: new FormControl(restaurantRawValue.isDedicatedGlutenFree, {
        validators: [Validators.required],
      }),
      website: new FormControl(restaurantRawValue.website),
      tableNumber: new FormControl(restaurantRawValue.tableNumber, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(restaurantRawValue.createdBy),
      createdDate: new FormControl(restaurantRawValue.createdDate),
      lastModifiedBy: new FormControl(restaurantRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(restaurantRawValue.lastModifiedDate),
    });
  }

  getRestaurant(form: RestaurantFormGroup): IRestaurant | NewRestaurant {
    return this.convertRestaurantRawValueToRestaurant(form.getRawValue() as RestaurantFormRawValue | NewRestaurantFormRawValue);
  }

  resetForm(form: RestaurantFormGroup, restaurant: RestaurantFormGroupInput): void {
    const restaurantRawValue = this.convertRestaurantToRestaurantRawValue({ ...this.getFormDefaults(), ...restaurant });
    form.reset(
      {
        ...restaurantRawValue,
        id: { value: restaurantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RestaurantFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isDedicatedGlutenFree: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertRestaurantRawValueToRestaurant(
    rawRestaurant: RestaurantFormRawValue | NewRestaurantFormRawValue
  ): IRestaurant | NewRestaurant {
    return {
      ...rawRestaurant,
      createdDate: dayjs(rawRestaurant.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawRestaurant.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertRestaurantToRestaurantRawValue(
    restaurant: IRestaurant | (Partial<NewRestaurant> & RestaurantFormDefaults)
  ): RestaurantFormRawValue | PartialWithRequiredKeyOf<NewRestaurantFormRawValue> {
    return {
      ...restaurant,
      createdDate: restaurant.createdDate ? restaurant.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: restaurant.lastModifiedDate ? restaurant.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
