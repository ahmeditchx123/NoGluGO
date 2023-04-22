import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';

@Injectable({ providedIn: 'root' })
export class MenuItemRoutingResolveService implements Resolve<IMenuItem | null> {
  constructor(protected service: MenuItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMenuItem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((menuItem: HttpResponse<IMenuItem>) => {
          if (menuItem.body) {
            return of(menuItem.body);
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
