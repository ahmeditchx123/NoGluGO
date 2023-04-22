import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMenuItem, NewMenuItem } from '../menu-item.model';

export type PartialUpdateMenuItem = Partial<IMenuItem> & Pick<IMenuItem, 'id'>;

type RestOf<T extends IMenuItem | NewMenuItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestMenuItem = RestOf<IMenuItem>;

export type NewRestMenuItem = RestOf<NewMenuItem>;

export type PartialUpdateRestMenuItem = RestOf<PartialUpdateMenuItem>;

export type EntityResponseType = HttpResponse<IMenuItem>;
export type EntityArrayResponseType = HttpResponse<IMenuItem[]>;

@Injectable({ providedIn: 'root' })
export class MenuItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/menu-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(menuItem: NewMenuItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menuItem);
    return this.http
      .post<RestMenuItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(menuItem: IMenuItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menuItem);
    return this.http
      .put<RestMenuItem>(`${this.resourceUrl}/${this.getMenuItemIdentifier(menuItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(menuItem: PartialUpdateMenuItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menuItem);
    return this.http
      .patch<RestMenuItem>(`${this.resourceUrl}/${this.getMenuItemIdentifier(menuItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestMenuItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMenuItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMenuItemIdentifier(menuItem: Pick<IMenuItem, 'id'>): string {
    return menuItem.id;
  }

  compareMenuItem(o1: Pick<IMenuItem, 'id'> | null, o2: Pick<IMenuItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getMenuItemIdentifier(o1) === this.getMenuItemIdentifier(o2) : o1 === o2;
  }

  addMenuItemToCollectionIfMissing<Type extends Pick<IMenuItem, 'id'>>(
    menuItemCollection: Type[],
    ...menuItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const menuItems: Type[] = menuItemsToCheck.filter(isPresent);
    if (menuItems.length > 0) {
      const menuItemCollectionIdentifiers = menuItemCollection.map(menuItemItem => this.getMenuItemIdentifier(menuItemItem)!);
      const menuItemsToAdd = menuItems.filter(menuItemItem => {
        const menuItemIdentifier = this.getMenuItemIdentifier(menuItemItem);
        if (menuItemCollectionIdentifiers.includes(menuItemIdentifier)) {
          return false;
        }
        menuItemCollectionIdentifiers.push(menuItemIdentifier);
        return true;
      });
      return [...menuItemsToAdd, ...menuItemCollection];
    }
    return menuItemCollection;
  }

  protected convertDateFromClient<T extends IMenuItem | NewMenuItem | PartialUpdateMenuItem>(menuItem: T): RestOf<T> {
    return {
      ...menuItem,
      createdDate: menuItem.createdDate?.toJSON() ?? null,
      lastModifiedDate: menuItem.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMenuItem: RestMenuItem): IMenuItem {
    return {
      ...restMenuItem,
      createdDate: restMenuItem.createdDate ? dayjs(restMenuItem.createdDate) : undefined,
      lastModifiedDate: restMenuItem.lastModifiedDate ? dayjs(restMenuItem.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMenuItem>): HttpResponse<IMenuItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMenuItem[]>): HttpResponse<IMenuItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
