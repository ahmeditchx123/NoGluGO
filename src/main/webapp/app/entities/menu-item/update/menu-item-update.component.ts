import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MenuItemFormService, MenuItemFormGroup } from './menu-item-form.service';
import { IMenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-menu-item-update',
  templateUrl: './menu-item-update.component.html',
})
export class MenuItemUpdateComponent implements OnInit {
  isSaving = false;
  menuItem: IMenuItem | null = null;

  menusSharedCollection: IMenu[] = [];

  editForm: MenuItemFormGroup = this.menuItemFormService.createMenuItemFormGroup();

  constructor(
    protected menuItemService: MenuItemService,
    protected menuItemFormService: MenuItemFormService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMenu = (o1: IMenu | null, o2: IMenu | null): boolean => this.menuService.compareMenu(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItem }) => {
      this.menuItem = menuItem;
      if (menuItem) {
        this.updateForm(menuItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menuItem = this.menuItemFormService.getMenuItem(this.editForm);
    if (menuItem.id !== null) {
      this.subscribeToSaveResponse(this.menuItemService.update(menuItem));
    } else {
      this.subscribeToSaveResponse(this.menuItemService.create(menuItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenuItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(menuItem: IMenuItem): void {
    this.menuItem = menuItem;
    this.menuItemFormService.resetForm(this.editForm, menuItem);

    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing<IMenu>(this.menusSharedCollection, menuItem.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing<IMenu>(menus, this.menuItem?.menu)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }
}
