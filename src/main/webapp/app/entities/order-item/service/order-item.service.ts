import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrderItem, NewOrderItem } from '../order-item.model';

export type PartialUpdateOrderItem = Partial<IOrderItem> & Pick<IOrderItem, 'id'>;

type RestOf<T extends IOrderItem | NewOrderItem> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestOrderItem = RestOf<IOrderItem>;

export type NewRestOrderItem = RestOf<NewOrderItem>;

export type PartialUpdateRestOrderItem = RestOf<PartialUpdateOrderItem>;

export type EntityResponseType = HttpResponse<IOrderItem>;
export type EntityArrayResponseType = HttpResponse<IOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class OrderItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/order-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orderItem: NewOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .post<RestOrderItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orderItem: IOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .put<RestOrderItem>(`${this.resourceUrl}/${this.getOrderItemIdentifier(orderItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orderItem: PartialUpdateOrderItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orderItem);
    return this.http
      .patch<RestOrderItem>(`${this.resourceUrl}/${this.getOrderItemIdentifier(orderItem)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrderItemIdentifier(orderItem: Pick<IOrderItem, 'id'>): string {
    return orderItem.id;
  }

  compareOrderItem(o1: Pick<IOrderItem, 'id'> | null, o2: Pick<IOrderItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrderItemIdentifier(o1) === this.getOrderItemIdentifier(o2) : o1 === o2;
  }

  addOrderItemToCollectionIfMissing<Type extends Pick<IOrderItem, 'id'>>(
    orderItemCollection: Type[],
    ...orderItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orderItems: Type[] = orderItemsToCheck.filter(isPresent);
    if (orderItems.length > 0) {
      const orderItemCollectionIdentifiers = orderItemCollection.map(orderItemItem => this.getOrderItemIdentifier(orderItemItem)!);
      const orderItemsToAdd = orderItems.filter(orderItemItem => {
        const orderItemIdentifier = this.getOrderItemIdentifier(orderItemItem);
        if (orderItemCollectionIdentifiers.includes(orderItemIdentifier)) {
          return false;
        }
        orderItemCollectionIdentifiers.push(orderItemIdentifier);
        return true;
      });
      return [...orderItemsToAdd, ...orderItemCollection];
    }
    return orderItemCollection;
  }

  protected convertDateFromClient<T extends IOrderItem | NewOrderItem | PartialUpdateOrderItem>(orderItem: T): RestOf<T> {
    return {
      ...orderItem,
      createdDate: orderItem.createdDate?.toJSON() ?? null,
      lastModifiedDate: orderItem.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOrderItem: RestOrderItem): IOrderItem {
    return {
      ...restOrderItem,
      createdDate: restOrderItem.createdDate ? dayjs(restOrderItem.createdDate) : undefined,
      lastModifiedDate: restOrderItem.lastModifiedDate ? dayjs(restOrderItem.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrderItem>): HttpResponse<IOrderItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrderItem[]>): HttpResponse<IOrderItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
