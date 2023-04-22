import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IStore, NewStore } from '../store.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStore for edit and NewStoreFormGroupInput for create.
 */
type StoreFormGroupInput = IStore | PartialWithRequiredKeyOf<NewStore>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IStore | NewStore> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type StoreFormRawValue = FormValueOf<IStore>;

type NewStoreFormRawValue = FormValueOf<NewStore>;

type StoreFormDefaults = Pick<NewStore, 'id' | 'isDedicatedGlutenFree' | 'hasDeliveryMode' | 'createdDate' | 'lastModifiedDate'>;

type StoreFormGroupContent = {
  id: FormControl<StoreFormRawValue['id'] | NewStore['id']>;
  name: FormControl<StoreFormRawValue['name']>;
  description: FormControl<StoreFormRawValue['description']>;
  telephone: FormControl<StoreFormRawValue['telephone']>;
  imgPath: FormControl<StoreFormRawValue['imgPath']>;
  isDedicatedGlutenFree: FormControl<StoreFormRawValue['isDedicatedGlutenFree']>;
  website: FormControl<StoreFormRawValue['website']>;
  hasDeliveryMode: FormControl<StoreFormRawValue['hasDeliveryMode']>;
  createdBy: FormControl<StoreFormRawValue['createdBy']>;
  createdDate: FormControl<StoreFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<StoreFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<StoreFormRawValue['lastModifiedDate']>;
};

export type StoreFormGroup = FormGroup<StoreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StoreFormService {
  createStoreFormGroup(store: StoreFormGroupInput = { id: null }): StoreFormGroup {
    const storeRawValue = this.convertStoreToStoreRawValue({
      ...this.getFormDefaults(),
      ...store,
    });
    return new FormGroup<StoreFormGroupContent>({
      id: new FormControl(
        { value: storeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(storeRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(storeRawValue.description),
      telephone: new FormControl(storeRawValue.telephone, {
        validators: [Validators.required],
      }),
      imgPath: new FormControl(storeRawValue.imgPath, {
        validators: [Validators.required],
      }),
      isDedicatedGlutenFree: new FormControl(storeRawValue.isDedicatedGlutenFree, {
        validators: [Validators.required],
      }),
      website: new FormControl(storeRawValue.website),
      hasDeliveryMode: new FormControl(storeRawValue.hasDeliveryMode, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(storeRawValue.createdBy),
      createdDate: new FormControl(storeRawValue.createdDate),
      lastModifiedBy: new FormControl(storeRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(storeRawValue.lastModifiedDate),
    });
  }

  getStore(form: StoreFormGroup): IStore | NewStore {
    return this.convertStoreRawValueToStore(form.getRawValue() as StoreFormRawValue | NewStoreFormRawValue);
  }

  resetForm(form: StoreFormGroup, store: StoreFormGroupInput): void {
    const storeRawValue = this.convertStoreToStoreRawValue({ ...this.getFormDefaults(), ...store });
    form.reset(
      {
        ...storeRawValue,
        id: { value: storeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StoreFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isDedicatedGlutenFree: false,
      hasDeliveryMode: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertStoreRawValueToStore(rawStore: StoreFormRawValue | NewStoreFormRawValue): IStore | NewStore {
    return {
      ...rawStore,
      createdDate: dayjs(rawStore.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawStore.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertStoreToStoreRawValue(
    store: IStore | (Partial<NewStore> & StoreFormDefaults)
  ): StoreFormRawValue | PartialWithRequiredKeyOf<NewStoreFormRawValue> {
    return {
      ...store,
      createdDate: store.createdDate ? store.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: store.lastModifiedDate ? store.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
