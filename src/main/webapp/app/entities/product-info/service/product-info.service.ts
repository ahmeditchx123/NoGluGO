import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductInfo, NewProductInfo } from '../product-info.model';

export type PartialUpdateProductInfo = Partial<IProductInfo> & Pick<IProductInfo, 'id'>;

type RestOf<T extends IProductInfo | NewProductInfo> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestProductInfo = RestOf<IProductInfo>;

export type NewRestProductInfo = RestOf<NewProductInfo>;

export type PartialUpdateRestProductInfo = RestOf<PartialUpdateProductInfo>;

export type EntityResponseType = HttpResponse<IProductInfo>;
export type EntityArrayResponseType = HttpResponse<IProductInfo[]>;

@Injectable({ providedIn: 'root' })
export class ProductInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productInfo: NewProductInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productInfo);
    return this.http
      .post<RestProductInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productInfo: IProductInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productInfo);
    return this.http
      .put<RestProductInfo>(`${this.resourceUrl}/${this.getProductInfoIdentifier(productInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productInfo: PartialUpdateProductInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productInfo);
    return this.http
      .patch<RestProductInfo>(`${this.resourceUrl}/${this.getProductInfoIdentifier(productInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestProductInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductInfoIdentifier(productInfo: Pick<IProductInfo, 'id'>): string {
    return productInfo.id;
  }

  compareProductInfo(o1: Pick<IProductInfo, 'id'> | null, o2: Pick<IProductInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductInfoIdentifier(o1) === this.getProductInfoIdentifier(o2) : o1 === o2;
  }

  addProductInfoToCollectionIfMissing<Type extends Pick<IProductInfo, 'id'>>(
    productInfoCollection: Type[],
    ...productInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productInfos: Type[] = productInfosToCheck.filter(isPresent);
    if (productInfos.length > 0) {
      const productInfoCollectionIdentifiers = productInfoCollection.map(
        productInfoItem => this.getProductInfoIdentifier(productInfoItem)!
      );
      const productInfosToAdd = productInfos.filter(productInfoItem => {
        const productInfoIdentifier = this.getProductInfoIdentifier(productInfoItem);
        if (productInfoCollectionIdentifiers.includes(productInfoIdentifier)) {
          return false;
        }
        productInfoCollectionIdentifiers.push(productInfoIdentifier);
        return true;
      });
      return [...productInfosToAdd, ...productInfoCollection];
    }
    return productInfoCollection;
  }

  protected convertDateFromClient<T extends IProductInfo | NewProductInfo | PartialUpdateProductInfo>(productInfo: T): RestOf<T> {
    return {
      ...productInfo,
      createdDate: productInfo.createdDate?.toJSON() ?? null,
      lastModifiedDate: productInfo.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProductInfo: RestProductInfo): IProductInfo {
    return {
      ...restProductInfo,
      createdDate: restProductInfo.createdDate ? dayjs(restProductInfo.createdDate) : undefined,
      lastModifiedDate: restProductInfo.lastModifiedDate ? dayjs(restProductInfo.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductInfo>): HttpResponse<IProductInfo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductInfo[]>): HttpResponse<IProductInfo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
