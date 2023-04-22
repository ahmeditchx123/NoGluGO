import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IGlutenProfile, NewGlutenProfile } from '../gluten-profile.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGlutenProfile for edit and NewGlutenProfileFormGroupInput for create.
 */
type GlutenProfileFormGroupInput = IGlutenProfile | PartialWithRequiredKeyOf<NewGlutenProfile>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IGlutenProfile | NewGlutenProfile> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type GlutenProfileFormRawValue = FormValueOf<IGlutenProfile>;

type NewGlutenProfileFormRawValue = FormValueOf<NewGlutenProfile>;

type GlutenProfileFormDefaults = Pick<NewGlutenProfile, 'id' | 'createdDate' | 'lastModifiedDate'>;

type GlutenProfileFormGroupContent = {
  id: FormControl<GlutenProfileFormRawValue['id'] | NewGlutenProfile['id']>;
  diseas: FormControl<GlutenProfileFormRawValue['diseas']>;
  otherDiseas: FormControl<GlutenProfileFormRawValue['otherDiseas']>;
  strictnessLevel: FormControl<GlutenProfileFormRawValue['strictnessLevel']>;
  diaryFreePreferenceLvl: FormControl<GlutenProfileFormRawValue['diaryFreePreferenceLvl']>;
  veganPreferenceLvl: FormControl<GlutenProfileFormRawValue['veganPreferenceLvl']>;
  ketoPreferenceLvl: FormControl<GlutenProfileFormRawValue['ketoPreferenceLvl']>;
  createdBy: FormControl<GlutenProfileFormRawValue['createdBy']>;
  createdDate: FormControl<GlutenProfileFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<GlutenProfileFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<GlutenProfileFormRawValue['lastModifiedDate']>;
  userId: FormControl<GlutenProfileFormRawValue['userId']>;
};

export type GlutenProfileFormGroup = FormGroup<GlutenProfileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GlutenProfileFormService {
  createGlutenProfileFormGroup(glutenProfile: GlutenProfileFormGroupInput = { id: null }): GlutenProfileFormGroup {
    const glutenProfileRawValue = this.convertGlutenProfileToGlutenProfileRawValue({
      ...this.getFormDefaults(),
      ...glutenProfile,
    });
    return new FormGroup<GlutenProfileFormGroupContent>({
      id: new FormControl(
        { value: glutenProfileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      diseas: new FormControl(glutenProfileRawValue.diseas),
      otherDiseas: new FormControl(glutenProfileRawValue.otherDiseas),
      strictnessLevel: new FormControl(glutenProfileRawValue.strictnessLevel),
      diaryFreePreferenceLvl: new FormControl(glutenProfileRawValue.diaryFreePreferenceLvl),
      veganPreferenceLvl: new FormControl(glutenProfileRawValue.veganPreferenceLvl),
      ketoPreferenceLvl: new FormControl(glutenProfileRawValue.ketoPreferenceLvl),
      createdBy: new FormControl(glutenProfileRawValue.createdBy),
      createdDate: new FormControl(glutenProfileRawValue.createdDate),
      lastModifiedBy: new FormControl(glutenProfileRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(glutenProfileRawValue.lastModifiedDate),
      userId: new FormControl(glutenProfileRawValue.userId),
    });
  }

  getGlutenProfile(form: GlutenProfileFormGroup): IGlutenProfile | NewGlutenProfile {
    return this.convertGlutenProfileRawValueToGlutenProfile(form.getRawValue() as GlutenProfileFormRawValue | NewGlutenProfileFormRawValue);
  }

  resetForm(form: GlutenProfileFormGroup, glutenProfile: GlutenProfileFormGroupInput): void {
    const glutenProfileRawValue = this.convertGlutenProfileToGlutenProfileRawValue({ ...this.getFormDefaults(), ...glutenProfile });
    form.reset(
      {
        ...glutenProfileRawValue,
        id: { value: glutenProfileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GlutenProfileFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertGlutenProfileRawValueToGlutenProfile(
    rawGlutenProfile: GlutenProfileFormRawValue | NewGlutenProfileFormRawValue
  ): IGlutenProfile | NewGlutenProfile {
    return {
      ...rawGlutenProfile,
      createdDate: dayjs(rawGlutenProfile.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawGlutenProfile.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertGlutenProfileToGlutenProfileRawValue(
    glutenProfile: IGlutenProfile | (Partial<NewGlutenProfile> & GlutenProfileFormDefaults)
  ): GlutenProfileFormRawValue | PartialWithRequiredKeyOf<NewGlutenProfileFormRawValue> {
    return {
      ...glutenProfile,
      createdDate: glutenProfile.createdDate ? glutenProfile.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: glutenProfile.lastModifiedDate ? glutenProfile.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
