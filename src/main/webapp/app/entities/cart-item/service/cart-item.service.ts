import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICartItem, NewCartItem } from '../cart-item.model';

export type PartialUpdateCartItem = Partial<ICartItem> & Pick<ICartItem, 'id'>;

type RestOf<T extends ICartItem | NewCartItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestCartItem = RestOf<ICartItem>;

export type NewRestCartItem = RestOf<NewCartItem>;

export type PartialUpdateRestCartItem = RestOf<PartialUpdateCartItem>;

export type EntityResponseType = HttpResponse<ICartItem>;
export type EntityArrayResponseType = HttpResponse<ICartItem[]>;

@Injectable({ providedIn: 'root' })
export class CartItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cart-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cartItem: NewCartItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cartItem);
    return this.http
      .post<RestCartItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cartItem: ICartItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cartItem);
    return this.http
      .put<RestCartItem>(`${this.resourceUrl}/${this.getCartItemIdentifier(cartItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cartItem: PartialUpdateCartItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cartItem);
    return this.http
      .patch<RestCartItem>(`${this.resourceUrl}/${this.getCartItemIdentifier(cartItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestCartItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCartItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCartItemIdentifier(cartItem: Pick<ICartItem, 'id'>): string {
    return cartItem.id;
  }

  compareCartItem(o1: Pick<ICartItem, 'id'> | null, o2: Pick<ICartItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getCartItemIdentifier(o1) === this.getCartItemIdentifier(o2) : o1 === o2;
  }

  addCartItemToCollectionIfMissing<Type extends Pick<ICartItem, 'id'>>(
    cartItemCollection: Type[],
    ...cartItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cartItems: Type[] = cartItemsToCheck.filter(isPresent);
    if (cartItems.length > 0) {
      const cartItemCollectionIdentifiers = cartItemCollection.map(cartItemItem => this.getCartItemIdentifier(cartItemItem)!);
      const cartItemsToAdd = cartItems.filter(cartItemItem => {
        const cartItemIdentifier = this.getCartItemIdentifier(cartItemItem);
        if (cartItemCollectionIdentifiers.includes(cartItemIdentifier)) {
          return false;
        }
        cartItemCollectionIdentifiers.push(cartItemIdentifier);
        return true;
      });
      return [...cartItemsToAdd, ...cartItemCollection];
    }
    return cartItemCollection;
  }

  protected convertDateFromClient<T extends ICartItem | NewCartItem | PartialUpdateCartItem>(cartItem: T): RestOf<T> {
    return {
      ...cartItem,
      createdDate: cartItem.createdDate?.toJSON() ?? null,
      lastModifiedDate: cartItem.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCartItem: RestCartItem): ICartItem {
    return {
      ...restCartItem,
      createdDate: restCartItem.createdDate ? dayjs(restCartItem.createdDate) : undefined,
      lastModifiedDate: restCartItem.lastModifiedDate ? dayjs(restCartItem.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCartItem>): HttpResponse<ICartItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCartItem[]>): HttpResponse<ICartItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
