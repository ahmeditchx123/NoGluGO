import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMenuItem, NewMenuItem } from '../menu-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMenuItem for edit and NewMenuItemFormGroupInput for create.
 */
type MenuItemFormGroupInput = IMenuItem | PartialWithRequiredKeyOf<NewMenuItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMenuItem | NewMenuItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type MenuItemFormRawValue = FormValueOf<IMenuItem>;

type NewMenuItemFormRawValue = FormValueOf<NewMenuItem>;

type MenuItemFormDefaults = Pick<NewMenuItem, 'id' | 'createdDate' | 'lastModifiedDate'>;

type MenuItemFormGroupContent = {
  id: FormControl<MenuItemFormRawValue['id'] | NewMenuItem['id']>;
  name: FormControl<MenuItemFormRawValue['name']>;
  content: FormControl<MenuItemFormRawValue['content']>;
  imgPath: FormControl<MenuItemFormRawValue['imgPath']>;
  unitPrice: FormControl<MenuItemFormRawValue['unitPrice']>;
  createdBy: FormControl<MenuItemFormRawValue['createdBy']>;
  createdDate: FormControl<MenuItemFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<MenuItemFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<MenuItemFormRawValue['lastModifiedDate']>;
  menu: FormControl<MenuItemFormRawValue['menu']>;
};

export type MenuItemFormGroup = FormGroup<MenuItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MenuItemFormService {
  createMenuItemFormGroup(menuItem: MenuItemFormGroupInput = { id: null }): MenuItemFormGroup {
    const menuItemRawValue = this.convertMenuItemToMenuItemRawValue({
      ...this.getFormDefaults(),
      ...menuItem,
    });
    return new FormGroup<MenuItemFormGroupContent>({
      id: new FormControl(
        { value: menuItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(menuItemRawValue.name, {
        validators: [Validators.required],
      }),
      content: new FormControl(menuItemRawValue.content, {
        validators: [Validators.required],
      }),
      imgPath: new FormControl(menuItemRawValue.imgPath, {
        validators: [Validators.required],
      }),
      unitPrice: new FormControl(menuItemRawValue.unitPrice, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(menuItemRawValue.createdBy),
      createdDate: new FormControl(menuItemRawValue.createdDate),
      lastModifiedBy: new FormControl(menuItemRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(menuItemRawValue.lastModifiedDate),
      menu: new FormControl(menuItemRawValue.menu),
    });
  }

  getMenuItem(form: MenuItemFormGroup): IMenuItem | NewMenuItem {
    return this.convertMenuItemRawValueToMenuItem(form.getRawValue() as MenuItemFormRawValue | NewMenuItemFormRawValue);
  }

  resetForm(form: MenuItemFormGroup, menuItem: MenuItemFormGroupInput): void {
    const menuItemRawValue = this.convertMenuItemToMenuItemRawValue({ ...this.getFormDefaults(), ...menuItem });
    form.reset(
      {
        ...menuItemRawValue,
        id: { value: menuItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MenuItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertMenuItemRawValueToMenuItem(rawMenuItem: MenuItemFormRawValue | NewMenuItemFormRawValue): IMenuItem | NewMenuItem {
    return {
      ...rawMenuItem,
      createdDate: dayjs(rawMenuItem.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawMenuItem.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMenuItemToMenuItemRawValue(
    menuItem: IMenuItem | (Partial<NewMenuItem> & MenuItemFormDefaults)
  ): MenuItemFormRawValue | PartialWithRequiredKeyOf<NewMenuItemFormRawValue> {
    return {
      ...menuItem,
      createdDate: menuItem.createdDate ? menuItem.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: menuItem.lastModifiedDate ? menuItem.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
