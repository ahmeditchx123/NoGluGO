import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGlutenProfile } from '../gluten-profile.model';
import { GlutenProfileService } from '../service/gluten-profile.service';

@Injectable({ providedIn: 'root' })
export class GlutenProfileRoutingResolveService implements Resolve<IGlutenProfile | null> {
  constructor(protected service: GlutenProfileService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGlutenProfile | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((glutenProfile: HttpResponse<IGlutenProfile>) => {
          if (glutenProfile.body) {
            return of(glutenProfile.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
