import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMenu, NewMenu } from '../menu.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMenu for edit and NewMenuFormGroupInput for create.
 */
type MenuFormGroupInput = IMenu | PartialWithRequiredKeyOf<NewMenu>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMenu | NewMenu> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type MenuFormRawValue = FormValueOf<IMenu>;

type NewMenuFormRawValue = FormValueOf<NewMenu>;

type MenuFormDefaults = Pick<NewMenu, 'id' | 'createdDate' | 'lastModifiedDate'>;

type MenuFormGroupContent = {
  id: FormControl<MenuFormRawValue['id'] | NewMenu['id']>;
  name: FormControl<MenuFormRawValue['name']>;
  description: FormControl<MenuFormRawValue['description']>;
  createdBy: FormControl<MenuFormRawValue['createdBy']>;
  createdDate: FormControl<MenuFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<MenuFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<MenuFormRawValue['lastModifiedDate']>;
  restaurant: FormControl<MenuFormRawValue['restaurant']>;
};

export type MenuFormGroup = FormGroup<MenuFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MenuFormService {
  createMenuFormGroup(menu: MenuFormGroupInput = { id: null }): MenuFormGroup {
    const menuRawValue = this.convertMenuToMenuRawValue({
      ...this.getFormDefaults(),
      ...menu,
    });
    return new FormGroup<MenuFormGroupContent>({
      id: new FormControl(
        { value: menuRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(menuRawValue.name, {
        validators: [Validators.required],
      }),
      description: new FormControl(menuRawValue.description),
      createdBy: new FormControl(menuRawValue.createdBy),
      createdDate: new FormControl(menuRawValue.createdDate),
      lastModifiedBy: new FormControl(menuRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(menuRawValue.lastModifiedDate),
      restaurant: new FormControl(menuRawValue.restaurant),
    });
  }

  getMenu(form: MenuFormGroup): IMenu | NewMenu {
    return this.convertMenuRawValueToMenu(form.getRawValue() as MenuFormRawValue | NewMenuFormRawValue);
  }

  resetForm(form: MenuFormGroup, menu: MenuFormGroupInput): void {
    const menuRawValue = this.convertMenuToMenuRawValue({ ...this.getFormDefaults(), ...menu });
    form.reset(
      {
        ...menuRawValue,
        id: { value: menuRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MenuFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertMenuRawValueToMenu(rawMenu: MenuFormRawValue | NewMenuFormRawValue): IMenu | NewMenu {
    return {
      ...rawMenu,
      createdDate: dayjs(rawMenu.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawMenu.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMenuToMenuRawValue(
    menu: IMenu | (Partial<NewMenu> & MenuFormDefaults)
  ): MenuFormRawValue | PartialWithRequiredKeyOf<NewMenuFormRawValue> {
    return {
      ...menu,
      createdDate: menu.createdDate ? menu.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: menu.lastModifiedDate ? menu.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
