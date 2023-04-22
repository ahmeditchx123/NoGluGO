import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMenu, NewMenu } from '../menu.model';

export type PartialUpdateMenu = Partial<IMenu> & Pick<IMenu, 'id'>;

type RestOf<T extends IMenu | NewMenu> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestMenu = RestOf<IMenu>;

export type NewRestMenu = RestOf<NewMenu>;

export type PartialUpdateRestMenu = RestOf<PartialUpdateMenu>;

export type EntityResponseType = HttpResponse<IMenu>;
export type EntityArrayResponseType = HttpResponse<IMenu[]>;

@Injectable({ providedIn: 'root' })
export class MenuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/menus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(menu: NewMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menu);
    return this.http.post<RestMenu>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(menu: IMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menu);
    return this.http
      .put<RestMenu>(`${this.resourceUrl}/${this.getMenuIdentifier(menu)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(menu: PartialUpdateMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menu);
    return this.http
      .patch<RestMenu>(`${this.resourceUrl}/${this.getMenuIdentifier(menu)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestMenu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMenu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMenuIdentifier(menu: Pick<IMenu, 'id'>): string {
    return menu.id;
  }

  compareMenu(o1: Pick<IMenu, 'id'> | null, o2: Pick<IMenu, 'id'> | null): boolean {
    return o1 && o2 ? this.getMenuIdentifier(o1) === this.getMenuIdentifier(o2) : o1 === o2;
  }

  addMenuToCollectionIfMissing<Type extends Pick<IMenu, 'id'>>(
    menuCollection: Type[],
    ...menusToCheck: (Type | null | undefined)[]
  ): Type[] {
    const menus: Type[] = menusToCheck.filter(isPresent);
    if (menus.length > 0) {
      const menuCollectionIdentifiers = menuCollection.map(menuItem => this.getMenuIdentifier(menuItem)!);
      const menusToAdd = menus.filter(menuItem => {
        const menuIdentifier = this.getMenuIdentifier(menuItem);
        if (menuCollectionIdentifiers.includes(menuIdentifier)) {
          return false;
        }
        menuCollectionIdentifiers.push(menuIdentifier);
        return true;
      });
      return [...menusToAdd, ...menuCollection];
    }
    return menuCollection;
  }

  protected convertDateFromClient<T extends IMenu | NewMenu | PartialUpdateMenu>(menu: T): RestOf<T> {
    return {
      ...menu,
      createdDate: menu.createdDate?.toJSON() ?? null,
      lastModifiedDate: menu.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMenu: RestMenu): IMenu {
    return {
      ...restMenu,
      createdDate: restMenu.createdDate ? dayjs(restMenu.createdDate) : undefined,
      lastModifiedDate: restMenu.lastModifiedDate ? dayjs(restMenu.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMenu>): HttpResponse<IMenu> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMenu[]>): HttpResponse<IMenu[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
