import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRestaurant, NewRestaurant } from '../restaurant.model';

export type PartialUpdateRestaurant = Partial<IRestaurant> & Pick<IRestaurant, 'id'>;

type RestOf<T extends IRestaurant | NewRestaurant> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestRestaurant = RestOf<IRestaurant>;

export type NewRestRestaurant = RestOf<NewRestaurant>;

export type PartialUpdateRestRestaurant = RestOf<PartialUpdateRestaurant>;

export type EntityResponseType = HttpResponse<IRestaurant>;
export type EntityArrayResponseType = HttpResponse<IRestaurant[]>;

@Injectable({ providedIn: 'root' })
export class RestaurantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/restaurants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(restaurant: NewRestaurant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurant);
    return this.http
      .post<RestRestaurant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(restaurant: IRestaurant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurant);
    return this.http
      .put<RestRestaurant>(`${this.resourceUrl}/${this.getRestaurantIdentifier(restaurant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(restaurant: PartialUpdateRestaurant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(restaurant);
    return this.http
      .patch<RestRestaurant>(`${this.resourceUrl}/${this.getRestaurantIdentifier(restaurant)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestRestaurant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRestaurant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRestaurantIdentifier(restaurant: Pick<IRestaurant, 'id'>): string {
    return restaurant.id;
  }

  compareRestaurant(o1: Pick<IRestaurant, 'id'> | null, o2: Pick<IRestaurant, 'id'> | null): boolean {
    return o1 && o2 ? this.getRestaurantIdentifier(o1) === this.getRestaurantIdentifier(o2) : o1 === o2;
  }

  addRestaurantToCollectionIfMissing<Type extends Pick<IRestaurant, 'id'>>(
    restaurantCollection: Type[],
    ...restaurantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const restaurants: Type[] = restaurantsToCheck.filter(isPresent);
    if (restaurants.length > 0) {
      const restaurantCollectionIdentifiers = restaurantCollection.map(restaurantItem => this.getRestaurantIdentifier(restaurantItem)!);
      const restaurantsToAdd = restaurants.filter(restaurantItem => {
        const restaurantIdentifier = this.getRestaurantIdentifier(restaurantItem);
        if (restaurantCollectionIdentifiers.includes(restaurantIdentifier)) {
          return false;
        }
        restaurantCollectionIdentifiers.push(restaurantIdentifier);
        return true;
      });
      return [...restaurantsToAdd, ...restaurantCollection];
    }
    return restaurantCollection;
  }

  protected convertDateFromClient<T extends IRestaurant | NewRestaurant | PartialUpdateRestaurant>(restaurant: T): RestOf<T> {
    return {
      ...restaurant,
      createdDate: restaurant.createdDate?.toJSON() ?? null,
      lastModifiedDate: restaurant.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRestaurant: RestRestaurant): IRestaurant {
    return {
      ...restRestaurant,
      createdDate: restRestaurant.createdDate ? dayjs(restRestaurant.createdDate) : undefined,
      lastModifiedDate: restRestaurant.lastModifiedDate ? dayjs(restRestaurant.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRestaurant>): HttpResponse<IRestaurant> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRestaurant[]>): HttpResponse<IRestaurant[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
