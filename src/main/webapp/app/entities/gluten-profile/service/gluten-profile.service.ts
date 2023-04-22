import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGlutenProfile, NewGlutenProfile } from '../gluten-profile.model';

export type PartialUpdateGlutenProfile = Partial<IGlutenProfile> & Pick<IGlutenProfile, 'id'>;

type RestOf<T extends IGlutenProfile | NewGlutenProfile> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestGlutenProfile = RestOf<IGlutenProfile>;

export type NewRestGlutenProfile = RestOf<NewGlutenProfile>;

export type PartialUpdateRestGlutenProfile = RestOf<PartialUpdateGlutenProfile>;

export type EntityResponseType = HttpResponse<IGlutenProfile>;
export type EntityArrayResponseType = HttpResponse<IGlutenProfile[]>;

@Injectable({ providedIn: 'root' })
export class GlutenProfileService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gluten-profiles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(glutenProfile: NewGlutenProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(glutenProfile);
    return this.http
      .post<RestGlutenProfile>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(glutenProfile: IGlutenProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(glutenProfile);
    return this.http
      .put<RestGlutenProfile>(`${this.resourceUrl}/${this.getGlutenProfileIdentifier(glutenProfile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(glutenProfile: PartialUpdateGlutenProfile): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(glutenProfile);
    return this.http
      .patch<RestGlutenProfile>(`${this.resourceUrl}/${this.getGlutenProfileIdentifier(glutenProfile)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestGlutenProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGlutenProfile[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGlutenProfileIdentifier(glutenProfile: Pick<IGlutenProfile, 'id'>): string {
    return glutenProfile.id;
  }

  compareGlutenProfile(o1: Pick<IGlutenProfile, 'id'> | null, o2: Pick<IGlutenProfile, 'id'> | null): boolean {
    return o1 && o2 ? this.getGlutenProfileIdentifier(o1) === this.getGlutenProfileIdentifier(o2) : o1 === o2;
  }

  addGlutenProfileToCollectionIfMissing<Type extends Pick<IGlutenProfile, 'id'>>(
    glutenProfileCollection: Type[],
    ...glutenProfilesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const glutenProfiles: Type[] = glutenProfilesToCheck.filter(isPresent);
    if (glutenProfiles.length > 0) {
      const glutenProfileCollectionIdentifiers = glutenProfileCollection.map(
        glutenProfileItem => this.getGlutenProfileIdentifier(glutenProfileItem)!
      );
      const glutenProfilesToAdd = glutenProfiles.filter(glutenProfileItem => {
        const glutenProfileIdentifier = this.getGlutenProfileIdentifier(glutenProfileItem);
        if (glutenProfileCollectionIdentifiers.includes(glutenProfileIdentifier)) {
          return false;
        }
        glutenProfileCollectionIdentifiers.push(glutenProfileIdentifier);
        return true;
      });
      return [...glutenProfilesToAdd, ...glutenProfileCollection];
    }
    return glutenProfileCollection;
  }

  protected convertDateFromClient<T extends IGlutenProfile | NewGlutenProfile | PartialUpdateGlutenProfile>(glutenProfile: T): RestOf<T> {
    return {
      ...glutenProfile,
      createdDate: glutenProfile.createdDate?.toJSON() ?? null,
      lastModifiedDate: glutenProfile.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restGlutenProfile: RestGlutenProfile): IGlutenProfile {
    return {
      ...restGlutenProfile,
      createdDate: restGlutenProfile.createdDate ? dayjs(restGlutenProfile.createdDate) : undefined,
      lastModifiedDate: restGlutenProfile.lastModifiedDate ? dayjs(restGlutenProfile.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestGlutenProfile>): HttpResponse<IGlutenProfile> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestGlutenProfile[]>): HttpResponse<IGlutenProfile[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
